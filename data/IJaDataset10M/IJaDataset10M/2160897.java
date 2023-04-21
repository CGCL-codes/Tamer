package jat.sim;

import jat.alg.estimators.EKF;
import jat.alg.integrators.LinePrinter;
import jat.cm.Constants;
import jat.forces.GravitationalBody;
import jat.forces.GravityModel;
import jat.forces.GravityModelType;
import jat.forces.HarrisPriester;
import jat.forces.Moon;
import jat.forces.NRLMSISE_Drag;
import jat.forces.SolarRadiationPressure;
import jat.forces.Sun;
import jat.matvec.data.Matrix;
import jat.matvec.data.VectorN;
import jat.measurements.OpticalMeasurementModel;
import jat.measurements.createMeasurements;
import jat.spacecraft.Spacecraft;
import jat.spacecraft.SpacecraftModel;
import jat.spacetime.LunaFixedRef;
import jat.spacetime.Time;
import jat.spacetime.TimeUtils;
import jat.spacetime.UniverseModel;
import jat.traj.RelativeTraj;
import jat.traj.Trajectory;
import jat.util.Celestia;
import jat.util.FileUtil;
import java.io.IOException;
import java.io.PrintStream;

public class CEVSim extends EstimatorSimModel {

    public static String JAT_name;

    private int numStates;

    public CEVSim() {
        super();
    }

    public CEVSim(boolean useFilter) {
        super(useFilter);
    }

    protected void initializeConst() {
        String fs, dir_in;
        fs = FileUtil.file_separator();
        try {
            dir_in = FileUtil.getClassFilePath("jat.sim", "SimModel") + "input" + fs;
        } catch (Exception e) {
            dir_in = "";
        }
        this.input = initializer.parse_file(dir_in + InputFile);
        double MJD0 = initializer.parseDouble(input, "init.MJD0");
        double T0 = initializer.parseDouble(input, "init.T0");
        double MJDF = initializer.parseDouble(input, "init.MJDF");
        double TF = initializer.parseDouble(input, "init.TF");
        this.mjd_utc_start = MJD0 + T0 / 86400;
        simTime = new Time(MJD0 + T0 / 86400);
        numSpacecraft = initializer.parseInt(input, "prop.NumSpacecraft");
        numStates = initializer.parseInt(input, "FILTER.states");
        dt = initializer.parseInt(input, "init.dt");
        if (JAT_runtruth) {
            truth = new SpacecraftModel[numSpacecraft];
            truth_traj = new Trajectory[numSpacecraft];
        }
        ref = new SpacecraftModel[numSpacecraft];
        ref_traj = new Trajectory[numSpacecraft];
        for (int i = 0; i < numSpacecraft; i++) {
            if (JAT_runtruth) truth_traj[i] = new Trajectory();
            ref_traj[i] = new Trajectory();
        }
        created_meas = new createMeasurements(input);
        filter = new EKF(input);
    }

    protected void initialize() {
        double[] r = new double[3];
        double[] tr = new double[3];
        double[] v = new double[3];
        double[] tv = new double[3];
        double cr, cd, area, mass, dt;
        double MJD0 = initializer.parseDouble(input, "init.MJD0") + initializer.parseDouble(input, "init.T0") / 86400.0;
        System.out.println("Propagating " + numSpacecraft + " Spacecraft");
        for (int i = 0; i < numSpacecraft; i++) {
            String refs = "REF_STATE.";
            String tru = "TRUE_STATE.";
            String str = refs + i + ".X";
            String strt = tru + i + ".X";
            r[0] = initializer.parseDouble(this.input, str);
            tr[0] = initializer.parseDouble(this.input, strt);
            str = refs + i + ".Y";
            strt = tru + i + ".Y";
            r[1] = initializer.parseDouble(this.input, str);
            tr[1] = initializer.parseDouble(this.input, strt);
            str = refs + i + ".Z";
            strt = tru + i + ".Z";
            r[2] = initializer.parseDouble(this.input, str);
            tr[2] = initializer.parseDouble(this.input, strt);
            str = refs + i + ".VX";
            strt = tru + i + ".VX";
            v[0] = initializer.parseDouble(this.input, str);
            tv[0] = initializer.parseDouble(this.input, strt);
            str = refs + i + ".VY";
            strt = tru + i + ".VY";
            v[1] = initializer.parseDouble(this.input, str);
            tv[1] = initializer.parseDouble(this.input, strt);
            str = refs + i + ".VZ";
            strt = tru + i + ".VZ";
            v[2] = initializer.parseDouble(this.input, str);
            tv[2] = initializer.parseDouble(this.input, strt);
            str = "jat." + i + ".Cr";
            cr = initializer.parseDouble(this.input, str);
            str = "jat." + i + ".Cd";
            cd = initializer.parseDouble(this.input, str);
            str = "jat." + i + ".mass";
            mass = initializer.parseDouble(this.input, str);
            str = "jat." + i + ".area";
            area = initializer.parseDouble(this.input, str);
            boolean[] force_flag = createForceFlag(i);
            VectorN rr = new VectorN(r);
            VectorN vv = new VectorN(v);
            Spacecraft s = new Spacecraft(rr, vv, cr, cd, area, mass);
            s.set_use_params_in_state(false);
            UniverseModel spacetime = createUniverseModel(MJD0, s, force_flag, gravityModel, "HP");
            spacetime.set_use_iers(false);
            ref[i] = new SpacecraftModel(s, spacetime);
            rr = new VectorN(tr);
            vv = new VectorN(tv);
            s = new Spacecraft(rr, vv, cr, cd, area, mass);
            s.set_use_params_in_state(false);
            truth[i] = new SpacecraftModel(s, spacetime);
            dt = initializer.parseInt(this.input, "init.dt");
            double range_noise = 0;
            double state_noise[] = new double[3];
            str = "MEAS.types";
            String str2;
            int numMeas = initializer.parseInt(this.input, str);
            for (int m = 0; m < numMeas; m++) {
                str = "MEAS." + m + ".desc";
                str2 = initializer.parseString(this.input, str);
                if (str2.equalsIgnoreCase("GPS")) {
                    str = "MEAS." + m + ".R";
                    range_noise = initializer.parseDouble(input, str);
                } else if (str2.equalsIgnoreCase("pseudoGPS")) {
                    int end = initializer.parseInt(input, "MEAS." + m + ".size");
                    state_noise = new double[end];
                    for (int snum = 0; snum < end; snum++) {
                        str = "MEAS." + m + ".R." + snum;
                        state_noise[snum] = initializer.parseDouble(input, str);
                    }
                }
            }
            ref[i].set_GPS_noise(state_noise, range_noise);
        }
    }

    public UniverseModel createUniverseModel(double mjd_utc, Spacecraft sc, boolean[] force_flag, boolean use_JGM2, String drag_model) {
        boolean use_LP165P = false;
        UniverseModel umodel = new UniverseModel(mjd_utc);
        VectorN zero = new VectorN(0, 0, 0);
        if (force_flag[0]) {
            System.out.println("Earth");
            GravitationalBody earth = new GravitationalBody(398600.4415e+9);
            umodel.addForce(earth);
        } else {
            if (use_JGM2) {
                System.out.println("JGM2");
                GravityModel earth_grav = new GravityModel(2, 2, GravityModelType.JGM2);
                umodel.addForce(earth_grav);
            } else {
                System.out.println("JGM3");
                GravityModel earth_grav = new GravityModel(20, 20, GravityModelType.JGM3);
                umodel.addForce(earth_grav);
            }
        }
        if (force_flag[1]) {
            System.out.println("Sun");
            umodel.set_compute_sun(true);
            Sun sun = new Sun(Constants.GM_Sun, zero, zero);
            umodel.addForce(sun);
        }
        if (force_flag[2]) {
            umodel.set_compute_moon(true);
            if (use_LP165P) {
                System.out.println("Moon - LP165P");
                GravityModel moon_grav = new GravityModel(2, 2, new LunaFixedRef(), "jat/forces/moonGravity/LP165P.grv");
                umodel.addForce(moon_grav);
            } else {
                System.out.println("Moon");
                Moon moon = new Moon(Constants.GM_Moon, zero, zero);
                umodel.addForce(moon);
            }
        }
        if (force_flag[3]) {
            double ap_opt = 14.918648166;
            double f107_opt = 150;
            double n_param_opt = 6;
            umodel.set_compute_sun(true);
            if (drag_model.endsWith("NRL") || drag_model.endsWith("A") || drag_model.endsWith("C")) {
                System.out.println("NRLMSISE");
                NRLMSISE_Drag drag = new NRLMSISE_Drag(sc);
                drag.setAP(ap_opt);
                drag.setF107Daily(f107_opt);
                drag.setF107Average(f107_opt);
                umodel.addForce(drag);
            } else {
                umodel.set_compute_sun(true);
                System.out.println("HarrisPriester");
                HarrisPriester atmos = new HarrisPriester(sc, 150);
                atmos.setParameter(n_param_opt);
                umodel.addForce(atmos);
            }
        }
        if (force_flag[4]) {
            umodel.set_compute_sun(true);
            System.out.println("SolarRadiationPressure");
            SolarRadiationPressure srp = new SolarRadiationPressure(sc);
            umodel.addForce(srp);
        }
        return umodel;
    }

    /**
	 * Propagation method for an individual spacecraft.  Increments the 
	 * model held in the spacecraft flight computer by a time 'dt'.
	 * Updates the computer's models according to the progression of time.
	 * Updates the spacecraft state.
	 * 
	 */
    public void step(SpacecraftModel sm) {
        double t = sm.get_sc_t();
        if (verbose_timestep) {
            System.out.println("step: " + t + " / " + tf + "    stepsize: " + dt);
        }
        rk8.setStepSize(dt);
        double[] X = new double[6];
        double[] Xnew = new double[6];
        double num_sc = 1;
        for (int i = 0; i < num_sc; i++) {
            Spacecraft s = sm.get_spacecraft();
            X = s.toStateVector(false);
            Xnew = rk8.step(t, X, sm);
            s.updateState(Xnew, false);
        }
        if (t > (tf - dt) && t != tf) {
            dt = (tf - t);
        }
        t = t + dt;
        sm.update(t);
        iteration++;
    }

    /**
	 * 
	 */
    protected void filter() {
        int numMeas = created_meas.getNumberMeasurements();
        VectorN newState = new VectorN(ref[0].get_spacecraft().toStateVector());
        int processedMeasurements = 0;
        for (int i = 0; i < numMeas; i++) {
            if (simTime.get_sim_time() % (created_meas.frequency[i]) == 0) {
                if (((OpticalMeasurementModel) created_meas.mm[i]).get_type() == OpticalMeasurementModel.TYPE_YANGLE_LOS) {
                    newState = filter.estimate(simTime.get_sim_time(), i, 0, useMeas);
                    processedMeasurements++;
                    newState = filter.estimate(simTime.get_sim_time(), i, 1, this.useMeas);
                    processedMeasurements++;
                } else {
                    newState = filter.estimate(simTime.get_sim_time(), i, 0, this.useMeas);
                    processedMeasurements++;
                }
            }
        }
        if (processedMeasurements == 0) newState = filter.estimate(simTime.get_sim_time(), 0, 0, false);
        double tmpState[] = new double[6];
        for (int numSats = 0; numSats < numSpacecraft; numSats++) {
            for (int i = 0; i < 6; i++) {
                tmpState[i] = newState.x[numSats * 6 + i];
            }
            ref[numSats].get_spacecraft().updateMotion(tmpState);
            double[] true_state = truth[numSats].get_spacecraft().toStateVector();
            VectorN vecTime = new VectorN(1, (simStep) * dt);
            VectorN trueState = new VectorN(true_state);
            VectorN truthOut = new VectorN(vecTime, trueState);
            new PrintStream(truths[numSats]).println(truthOut.toString());
            double[] ref_state = ref[numSats].get_spacecraft().toStateVector();
            VectorN vecState = new VectorN(ref_state);
            VectorN stateOut = new VectorN(vecTime, vecState);
            new PrintStream(trajectories[numSats]).println(stateOut.toString());
            VectorN error_out = new VectorN(6);
            for (int i = 0; i < 6; i++) {
                error_out.x[i] = true_state[i] - ref_state[i];
            }
            VectorN ErrState = new VectorN(error_out);
            stateOut = new VectorN(vecTime, ErrState);
            new PrintStream(ECIError[numSats]).println(stateOut.toString());
            Matrix Covariance = filter.get_pold();
            int numStates = filter.get_numStates();
            double[] tmp = new double[numStates * numStates];
            int k = 0;
            for (int i = 0; i < numStates; i++) {
                for (int j = 0; j < numStates; j++) {
                    tmp[k] = Covariance.get(i, j);
                    k++;
                }
            }
            VectorN ErrCov = new VectorN(tmp);
            stateOut = new VectorN(vecTime, ErrCov);
            new PrintStream(covariance[numSats]).println(stateOut.toString());
        }
    }

    public void runloop() {
        double start = System.currentTimeMillis();
        openFiles();
        initialize();
        for (int i = 0; i < numSpacecraft; i++) {
            truth_traj[i].add(truth[i].get_sc_t(), truth[i].get_spacecraft().toStateVector());
            ref_traj[i].add(ref[i].get_sc_t(), ref[i].get_spacecraft().toStateVector());
        }
        int filterMode = initializer.parseInt(this.input, "init.mode");
        double MJD0 = initializer.parseDouble(this.input, "init.MJD0");
        double MJDF = initializer.parseDouble(this.input, "init.MJDF");
        double T0 = initializer.parseDouble(this.input, "init.T0");
        double TF = initializer.parseDouble(this.input, "init.TF");
        this.mjd_utc_start = MJD0 + T0 / 86400.0;
        simTime = new Time(MJD0 + T0 / 86400.0);
        double simLength = Math.round((MJDF - MJD0) * 86400 + TF - T0);
        this.tf = simLength;
        set_verbose(this.verbose_estimation);
        for (simStep = 1; simStep < simLength / dt; simStep++) {
            propagate(simStep * dt);
            simTime.update(simStep * dt);
            filter();
            for (int i = 0; i < numSpacecraft; i++) {
                truth_traj[i].add(truth[i].get_sc_t(), truth[i].get_spacecraft().toStateVector());
                ref_traj[i].add(ref[i].get_sc_t(), ref[i].get_spacecraft().toStateVector());
            }
        }
        closeFiles();
        System.gc();
        double elapsed = (System.currentTimeMillis() - start) * 0.001 / 60;
        System.out.println("Elapsed time [min]: " + elapsed);
        LinePrinter lp = new LinePrinter();
        RelativeTraj[] reltraj = new RelativeTraj[numSpacecraft];
        double mismatch_tol = 0.00001;
        for (int i = 0; i < numSpacecraft; i++) {
            if (PlotJAT) {
                reltraj[i] = new RelativeTraj(ref_traj[i], truth_traj[i], lp, "Jat(Ref) v Jat(Truth)");
                reltraj[i].setVerbose(false);
                reltraj[i].process(mismatch_tol);
                reltraj[i].process_RSS(mismatch_tol);
            }
            try {
                Celestia cel = new Celestia("C:/Code/Celestia/");
                cel.set_trajectory_meters(ref_traj[i], MJD0);
                cel.write_trajectory("jat_ref_" + JAT_name + JAT_case, "jat_ref_" + JAT_name + JAT_case, TimeUtils.MJDtoJD(this.mjd_utc_start));
                cel.set_trajectory_meters(truth_traj[i], MJD0);
                cel.write_trajectory("jat_truth_" + JAT_name + JAT_case, "jat_truth_" + JAT_name + JAT_case, TimeUtils.MJDtoJD(this.mjd_utc_start));
            } catch (IOException e) {
                System.err.println("Couldn't write to Celestia.");
            }
        }
    }

    public void set_verbose(boolean b) {
        this.verbose_estimation = b;
        if (filter != null) filter.set_verbose(b, this.tf);
    }

    public static void main(String[] args) {
        boolean useFilter = true;
        EstimatorSimModel.JAT_case = 44;
        CEVSim.JAT_name = "moon2earth_";
        CEVSim.InputFile = "initialConditions_cev_m2e_HIGH.txt";
        CEVSim.PlotJAT = true;
        CEVSim Sim = new CEVSim(useFilter);
        Sim.set_verbose(true);
        Sim.runloop();
        if (OpticalMeasurementModel.fobs != null) {
            OpticalMeasurementModel.fobs.close();
        }
        if (OpticalMeasurementModel.fpred != null) {
            OpticalMeasurementModel.fpred.close();
        }
        System.out.println("Finished.");
    }
}
