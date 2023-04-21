package jpfESAS;

import gov.nasa.jpf.sc.*;

/**
 * simplified state machine for CEV 1.5 EOR-LOR mission profile
 * 
 * we model the following aspects:
 *   (1) flight phases (Ascent, EarthOrbit, TransitEarthMoon etc.)
 *   (2) events (srbIgnition, stage1Separation, lsamRendezvous etc.)
 *   (3) vehicle configuration (via controlled object 'spacecraft')
 *   (4) failures (via controlled object 'failures')
 *   
 * this example requires extensions/statechart to be compiled
 */
public class CEV_15EOR_LOR extends State {

    ErrorLog errors = new ErrorLog();

    Failures failures = new Failures(errors);

    Spacecraft spacecraft = new Spacecraft(failures, errors);

    class Ascent extends State {

        class PrelaunchCheck extends State {

            public void srbIgnition() {
                setNextState(firstStage);
            }

            @Params("10|1")
            public void failure(int tminus) {
                if (tminus <= 5) {
                    setNextState(padAbort);
                } else {
                    setNextState(holdLaunch);
                }
            }
        }

        PrelaunchCheck prelaunchCheck = makeInitial(new PrelaunchCheck());

        class FirstStage extends State {

            public void stage1Separation() {
                spacecraft.doStage1Separation();
                setNextState(secondStage);
            }

            @Params("5000|120000|200000, true|false")
            public void abort(int altitude, boolean controlMotorFired) {
                spacecraft.doStage1Abort(altitude, controlMotorFired);
                if (altitude <= 120000) {
                    if (controlMotorFired) {
                        setNextState(abortLowActiveLAS);
                    } else {
                        setNextState(abortPassiveLAS);
                    }
                }
                if (altitude >= 120000) {
                    setNextState(abortHighActiveLAS);
                }
                assert hasNextState() : errors.log("abort command did not enter abort state");
            }
        }

        FirstStage firstStage = new FirstStage();

        class SecondStage extends State {

            public void lasJettison() {
                spacecraft.doLASjettison();
                setNextState(this);
            }

            public void stage2Separation() {
                spacecraft.doStage2Separation();
                setNextState(earthOrbit);
            }

            public void abort(boolean controlMotorFired) {
                spacecraft.doStage2Abort(controlMotorFired);
                if (controlMotorFired) {
                    setNextState(abortHighActiveLAS);
                } else {
                    setNextState(abortPassiveLAS);
                }
                assert hasNextState() : errors.log("abort command did not enter abort state");
            }
        }

        SecondStage secondStage = new SecondStage();

        class HoldLaunch extends State {

            public void completion() {
                setEndState();
            }
        }

        HoldLaunch holdLaunch = new HoldLaunch();

        class PadAbort extends State {

            public void completion() {
                setEndState();
            }
        }

        PadAbort padAbort = new PadAbort();

        class AbortPassiveLAS extends State {

            public void completion() {
                spacecraft.doLowPassiveAbort();
                setNextState(entry.chuteSequence);
            }
        }

        AbortPassiveLAS abortPassiveLAS = new AbortPassiveLAS();

        class AbortLowActiveLAS extends State {

            public void completion() {
                assert failures.noLAS_CNTRLfailure() : errors.log("active LAS with failed control motor");
                spacecraft.doLowActiveAbort();
                setNextState(entry.chuteSequence);
            }
        }

        AbortLowActiveLAS abortLowActiveLAS = new AbortLowActiveLAS();

        class AbortHighActiveLAS extends State {

            public void completion() {
                assert failures.noLAS_CNTRLfailure() : errors.log("active LAS with failed control motor");
                setNextState(entry.chuteSequence);
            }
        }

        AbortHighActiveLAS abortHighActiveLAS = new AbortHighActiveLAS();
    }

    Ascent ascent = makeInitial(new Ascent());

    class EarthOrbit extends State {

        class Insertion extends State {

            public void completion() {
                if (failures.noEARTH_SENSORfailure()) {
                    setNextState(orbitOps);
                } else {
                    setNextState(safeHold);
                }
            }
        }

        Insertion insertion = makeInitial(new Insertion());

        class OrbitOps extends State {

            public void lsamRendezvous() {
                assert spacecraft.readyForLSAMrendezvous() : errors.last();
                spacecraft.doLSAMrendezvous();
                setNextState(this);
            }

            public void tliBurn() {
                assert spacecraft.readyForTliBurn() : errors.last();
                setNextState(transitEarthMoon);
            }

            public void deorbit() {
                assert spacecraft.readyForDeorbit() : errors.last();
                setNextState(entry);
            }
        }

        OrbitOps orbitOps = new OrbitOps();

        class SafeHold extends State {

            public void enterOrbitOps() {
                if (failures.noEARTH_SENSORfailure()) {
                    setNextState(orbitOps);
                }
            }

            public void deorbit() {
                assert spacecraft.readyForDeorbit() : errors.last();
                setNextState(entry);
            }
        }

        SafeHold safeHold = new SafeHold();
    }

    EarthOrbit earthOrbit = new EarthOrbit();

    class TransitEarthMoon extends State {

        public void edsSeparation() {
            spacecraft.doEDSseparation();
            setNextState(this);
        }

        public void loiBurn() {
            setNextState(lunarOps);
        }
    }

    TransitEarthMoon transitEarthMoon = new TransitEarthMoon();

    class LunarOps extends State {

        class Insertion extends State {

            public void completion() {
                setNextState(lunarOrbit);
            }
        }

        Insertion insertion = makeInitial(new Insertion());

        class LunarOrbit extends State {

            public void lsamSeparation() {
                setNextState(lunarLanding);
            }

            public void teiBurn() {
                assert spacecraft.readyForTeiBurn() : errors.last();
                setNextState(transitMoonEarth);
            }
        }

        LunarOrbit lunarOrbit = new LunarOrbit();

        class LunarLanding extends State {

            public void completion() {
                setNextState(lunarOrbit);
            }

            class OrbitOpsLoiter extends State {

                public void lsamAscentRendezvous() {
                    setEndState();
                }
            }

            OrbitOpsLoiter orbitOpsLoiter = makeInitial(new OrbitOpsLoiter());

            class LunarDescent extends State {

                public void completion() {
                    setNextState(surfaceOps);
                }
            }

            LunarDescent lunarDescent = makeInitial(new LunarDescent());

            class SurfaceOps extends State {

                public void lsamAscentBurn() {
                    spacecraft.doLSAMascentBurn();
                    setNextState(lunarAscent);
                }
            }

            SurfaceOps surfaceOps = new SurfaceOps();

            class LunarAscent extends State {

                public void lsamAscentRendezvous() {
                    spacecraft.doLSAMascentRendezvous();
                    setEndState();
                }
            }

            LunarAscent lunarAscent = new LunarAscent();
        }

        LunarLanding lunarLanding = new LunarLanding();
    }

    LunarOps lunarOps = new LunarOps();

    class TransitMoonEarth extends State {

        public void smSeparation() {
            spacecraft.doSMseparation();
            setNextState(this);
        }

        public void eiBurn(boolean cmImbalance, boolean rcsFailure) {
            assert spacecraft.readyForEiBurn() : errors.last();
            spacecraft.doEiBurn(cmImbalance, rcsFailure);
            if (cmImbalance) {
                setNextState(entry.abortEntryBallistic);
            } else if (rcsFailure) {
                setNextState(entry.abortEntryFixedBank);
            } else {
                setNextState(entry);
            }
        }
    }

    TransitMoonEarth transitMoonEarth = new TransitMoonEarth();

    class Entry extends State {

        class EntryInterface extends State {

            public void completion() {
                setNextState(nominalEntry);
            }
        }

        EntryInterface entryInterface = makeInitial(new EntryInterface());

        class NominalEntry extends State {

            public void completion() {
                setNextState(chuteSequence);
            }
        }

        NominalEntry nominalEntry = new NominalEntry();

        class ChuteSequence extends State {

            public void completion() {
                assert spacecraft.readyForChuteSequence() : errors.last();
                setNextState(landing);
            }
        }

        ChuteSequence chuteSequence = new ChuteSequence();

        class Landing extends State {

            public void completion() {
                setEndState();
            }
        }

        Landing landing = new Landing();

        class AbortEntryBallistic extends State {

            public void completion() {
                setNextState(chuteSequence);
            }
        }

        AbortEntryBallistic abortEntryBallistic = new AbortEntryBallistic();

        class AbortEntryFixedBank extends State {

            public void completion() {
                setNextState(chuteSequence);
            }
        }

        AbortEntryFixedBank abortEntryFixedBank = new AbortEntryFixedBank();
    }

    Entry entry = new Entry();
}
