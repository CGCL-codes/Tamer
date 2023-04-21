package playground.dgrether.signalsystems.sylvia.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.signalsystems.data.signalcontrol.v20.SignalControlData;
import org.matsim.signalsystems.data.signalcontrol.v20.SignalControlDataFactory;
import org.matsim.signalsystems.data.signalcontrol.v20.SignalControlDataImpl;
import org.matsim.signalsystems.data.signalcontrol.v20.SignalControlReader20;
import org.matsim.signalsystems.data.signalcontrol.v20.SignalControlWriter20;
import org.matsim.signalsystems.data.signalcontrol.v20.SignalGroupSettingsData;
import org.matsim.signalsystems.data.signalcontrol.v20.SignalPlanData;
import org.matsim.signalsystems.data.signalcontrol.v20.SignalSystemControllerData;
import org.matsim.signalsystems.data.signalgroups.v20.SignalGroupsData;
import org.matsim.signalsystems.data.signalgroups.v20.SignalGroupsDataImpl;
import org.matsim.signalsystems.data.signalgroups.v20.SignalGroupsReader20;
import org.xml.sax.SAXException;
import playground.dgrether.DgPaths;
import playground.dgrether.signalsystems.sylvia.model.DgSylviaController;
import playground.dgrether.signalsystems.utils.DgSignalGroupSettingsDataOnsetComparator;
import playground.dgrether.signalsystems.utils.DgSignalsUtils;

/**
 * If I would have to write this again I would start with calculation of the IntergreenConstraints in this class and 
 * than follow a forward-propagation of constraints. but currently it is working fine!
 * @author dgrether
 *
 */
public class DgSylviaPreprocessData {

    private static final Logger log = Logger.getLogger(DgSylviaPreprocessData.class);

    public static final String FIXED_TIME_PREFIX = "fixed_time_plan_";

    public static final String SYLVIA_PREFIX = "sylvia_plan_";

    private int minGreenSeconds = 5;

    private Set<Id> signalSystemIds = new HashSet<Id>();

    private boolean createAll = true;

    public DgSylviaPreprocessData() {
        this.signalSystemIds.add(new IdImpl(8));
    }

    public void convertFixedTimePlansToSylviaBasePlans(String signalControlInputFile, String singalControlOutputFile, String signalGroupsFile, String signalGroupsOutFile) throws JAXBException, SAXException, ParserConfigurationException, IOException {
        SignalControlData signalControl = new SignalControlDataImpl();
        SignalControlReader20 reader = new SignalControlReader20(signalControl);
        reader.readFile(signalControlInputFile);
        SignalGroupsData signalGroupsData = new SignalGroupsDataImpl();
        SignalGroupsReader20 groupsReader = new SignalGroupsReader20(signalGroupsData);
        groupsReader.readFile(signalGroupsFile);
        SignalControlData newSignalControl = this.convertSignalControlData(signalControl);
        SignalControlWriter20 writer = new SignalControlWriter20(newSignalControl);
        writer.write(singalControlOutputFile);
    }

    /**
	 * Merge all signalGroups that have equal onsets and droppings to one SignalGroup, also change the SignalControlData accordingly
	 */
    private SignalGroupsData convertSignalGroupsData(SignalGroupsData signalGroupsData, SignalControlData signalControl) {
        for (SignalSystemControllerData controllerData : signalControl.getSignalSystemControllerDataBySystemId().values()) {
            if (controllerData.getSignalPlanData().size() > 1) {
                log.warn("More than one plan, check if this tool is doing the correct work!");
            }
            for (SignalPlanData signalPlan : controllerData.getSignalPlanData().values()) {
                Map<Integer, List<SignalGroupSettingsData>> onsetGroupSettingsMap = new HashMap<Integer, List<SignalGroupSettingsData>>();
                for (SignalGroupSettingsData signalGroupSettings : signalPlan.getSignalGroupSettingsDataByGroupId().values()) {
                    if (!onsetGroupSettingsMap.containsKey(signalGroupSettings.getOnset())) {
                        onsetGroupSettingsMap.put(signalGroupSettings.getOnset(), new ArrayList<SignalGroupSettingsData>());
                    }
                    onsetGroupSettingsMap.get(signalGroupSettings.getOnset()).add(signalGroupSettings);
                }
            }
        }
        return null;
    }

    private SignalControlData convertSignalControlData(final SignalControlData controlData) {
        SignalControlData cd = new SignalControlDataImpl();
        for (SignalSystemControllerData controllerData : controlData.getSignalSystemControllerDataBySystemId().values()) {
            if (!this.createAll && !this.signalSystemIds.contains(controllerData.getSignalSystemId())) {
                cd.addSignalSystemControllerData(controllerData);
            } else {
                SignalSystemControllerData newControllerData = cd.getFactory().createSignalSystemControllerData(controllerData.getSignalSystemId());
                cd.addSignalSystemControllerData(newControllerData);
                newControllerData.setControllerIdentifier(DgSylviaController.CONTROLLER_IDENTIFIER);
                log.debug("");
                log.debug("system: " + controllerData.getSignalSystemId());
                for (SignalPlanData signalPlan : controllerData.getSignalPlanData().values()) {
                    newControllerData.addSignalPlanData(DgSignalsUtils.copySignalPlanData(signalPlan, new IdImpl(FIXED_TIME_PREFIX + signalPlan.getId().toString()), cd.getFactory()));
                    newControllerData.addSignalPlanData(this.convertSignalPlanData(signalPlan, cd.getFactory()));
                }
            }
        }
        return cd;
    }

    private DgPhase createSylviaPhase(final DgPhase phase, final SignalControlDataFactory factory) {
        log.error("creating sylvia phase...");
        final int on = phase.getPhaseStartSecond();
        List<SignalGroupSettingsData> alltimeGreenSettings = new ArrayList<SignalGroupSettingsData>();
        List<SignalGroupSettingsData> shorterSettingsSortedByOnset = new ArrayList<SignalGroupSettingsData>();
        List<SignalGroupSettingsData> newSettings = new ArrayList<SignalGroupSettingsData>();
        for (SignalGroupSettingsData settings : phase.getSignalGroupSettingsByGroupId().values()) {
            if (settings.getOnset() == phase.getPhaseStartSecond() && settings.getDropping() == phase.getPhaseEndSecond()) {
                alltimeGreenSettings.add(settings);
            } else {
                shorterSettingsSortedByOnset.add(settings);
            }
        }
        Collections.sort(shorterSettingsSortedByOnset, new DgSignalGroupSettingsDataOnsetComparator());
        int currentShortOn = on;
        int currentOn = on;
        int currentShortOff = on + minGreenSeconds;
        for (SignalGroupSettingsData settings : shorterSettingsSortedByOnset) {
            SignalGroupSettingsData shortSettings = DgSignalsUtils.copySignalGroupSettingsData(settings, factory);
            if (settings.getOnset() != currentOn) {
                currentOn = settings.getOnset();
                currentShortOn = currentShortOn + minGreenSeconds;
                currentShortOff = currentShortOn + minGreenSeconds;
            }
            shortSettings.setOnset(currentShortOn);
            shortSettings.setDropping(currentShortOff);
            newSettings.add(shortSettings);
        }
        int off;
        if (currentShortOn == on && shorterSettingsSortedByOnset.size() > 0) {
            off = currentShortOff + minGreenSeconds;
        } else {
            off = currentShortOff;
        }
        DgPhase newPhase = new DgPhase(on, off);
        for (SignalGroupSettingsData settings : alltimeGreenSettings) {
            SignalGroupSettingsData shortSettings = DgSignalsUtils.copySignalGroupSettingsData(settings, factory);
            shortSettings.setOnset(on);
            shortSettings.setDropping(off);
            newSettings.add(shortSettings);
        }
        for (SignalGroupSettingsData settings : newSettings) {
            newPhase.addSignalGroupSettingsData(settings);
        }
        return newPhase;
    }

    private SignalPlanData convertSignalPlanData(final SignalPlanData fixedTimePlan, SignalControlDataFactory factory) {
        SignalPlanData newPlan = DgSignalsUtils.copySignalPlanData(fixedTimePlan, new IdImpl(SYLVIA_PREFIX + fixedTimePlan.getId().toString()), factory);
        List<SignalGroupSettingsData> groupSettingsList = new ArrayList<SignalGroupSettingsData>();
        groupSettingsList.addAll(newPlan.getSignalGroupSettingsDataByGroupId().values());
        Set<SignalGroupSettingsData> allGreenSettings = this.removeAllGreenSignalGroupSettings(groupSettingsList, fixedTimePlan.getCycleTime());
        List<DgPhase> phases = this.calculateSortedPhases(groupSettingsList);
        List<DgPhase> sylviaPhases = new ArrayList<DgPhase>();
        int phaseStart = 0;
        int lastPhaseOff = 0;
        for (int i = 0; i < phases.size(); i++) {
            System.out.println();
            log.error("Processing phase: " + (i + 1) + " of " + phases.size());
            final DgPhase phase = phases.get(i);
            DgPhase sylviaPhase = this.createSylviaPhase(phase, factory);
            if (i == 0) {
                phaseStart = phase.getPhaseStartSecond();
            } else {
                DgPhase lastPhase = phases.get(i - 1);
                DgPhase lastSylviaPhase = sylviaPhases.get(i - 1);
                log.error("last phase end: " + lastSylviaPhase.getPhaseEndSecond());
                int intergreen = phase.getPhaseStartSecond() - lastPhase.getPhaseEndSecond();
                log.error("intergreen: " + intergreen + " due to phase start at " + phase.getPhaseStartSecond() + " last phase end: " + lastPhase.getPhaseEndSecond());
                if (intergreen >= 0) {
                    log.info("phase not overlapping...");
                    phaseStart = lastSylviaPhase.getPhaseEndSecond() + intergreen;
                    int shift = sylviaPhase.getPhaseStartSecond() - phaseStart;
                    log.error("shift of not overlapping phase : " + shift);
                    sylviaPhase.setPhaseStartSecond(sylviaPhase.getPhaseStartSecond() - shift);
                    sylviaPhase.setPhaseEndSecond(sylviaPhase.getPhaseEndSecond() - shift);
                    for (SignalGroupSettingsData settings : sylviaPhase.getSignalGroupSettingsByGroupId().values()) {
                        settings.setOnset(settings.getOnset() - shift);
                        settings.setDropping(settings.getDropping() - shift);
                        log.info("  ...modified settings of phase: " + settings.getSignalGroupId() + " on:  " + settings.getOnset() + " off: " + settings.getDropping());
                    }
                } else {
                    log.error("phases overlap...");
                    Collection<IntergreenConstraint> intergreenConstraints = this.calculateIntergreenConstraints(lastPhase, phase);
                    log.error("intergreen constraints: " + intergreenConstraints);
                    if (!intergreenConstraints.isEmpty()) {
                        int phaseOn = sylviaPhase.getPhaseEndSecond();
                        int phaseOff = 0;
                        for (IntergreenConstraint ic : intergreenConstraints) {
                            SignalGroupSettingsData sylviaSettings = sylviaPhase.getSignalGroupSettingsByGroupId().get(ic.onSettingsId);
                            SignalGroupSettingsData lastSylviaSettings = lastSylviaPhase.getSignalGroupSettingsByGroupId().get(ic.droppingSettingsId);
                            int greenTime = sylviaSettings.getDropping() - sylviaSettings.getOnset();
                            int on = lastSylviaSettings.getDropping() + ic.intergreen;
                            int off = on + greenTime;
                            log.error("settings " + sylviaSettings.getSignalGroupId() + " green time : " + greenTime + " intergreen constraint: " + ic.intergreen + " to group id " + ic.droppingSettingsId + " on " + on + " off " + off);
                            sylviaSettings.setOnset(on);
                            sylviaSettings.setDropping(off);
                            if (on < phaseOn) phaseOn = on;
                            if (off > phaseOff) phaseOff = off;
                        }
                        log.error("shiftet phase to " + phaseOn + " - " + phaseOff);
                        sylviaPhase.setPhaseStartSecond(phaseOn);
                        sylviaPhase.setPhaseEndSecond(phaseOff);
                        for (SignalGroupSettingsData settings : sylviaPhase.getSignalGroupSettingsByGroupId().values()) {
                            settings.setDropping(sylviaPhase.getPhaseEndSecond());
                        }
                    } else {
                    }
                }
            }
            lastPhaseOff = phase.getPhaseEndSecond();
            sylviaPhases.add(sylviaPhase);
        }
        int lastIntergreen = fixedTimePlan.getCycleTime() - lastPhaseOff;
        int lastSylviaPhaseOff = 0;
        newPlan.getSignalGroupSettingsDataByGroupId().clear();
        for (DgPhase p : sylviaPhases) {
            this.addPhaseToPlan(p, newPlan);
            lastSylviaPhaseOff = p.getPhaseEndSecond();
        }
        int sylviaCycle = lastSylviaPhaseOff + lastIntergreen;
        newPlan.setCycleTime(sylviaCycle);
        for (SignalGroupSettingsData settings : allGreenSettings) {
            settings.setOnset(0);
            settings.setDropping(sylviaCycle);
            newPlan.addSignalGroupSettings(settings);
        }
        return newPlan;
    }

    class IntergreenConstraint {

        Id droppingSettingsId;

        Id onSettingsId;

        Integer intergreen;
    }

    /**
	 * calculates the time that should be between two phases that overlap in time
	 * @return 
	 */
    private Collection<IntergreenConstraint> calculateIntergreenConstraints(DgPhase lastPhase, DgPhase phase) {
        Map<SignalGroupSettingsData, IntergreenConstraint> map = new HashMap<SignalGroupSettingsData, IntergreenConstraint>();
        IntergreenConstraint ic = null;
        int intergreen;
        for (SignalGroupSettingsData settings : phase.getSignalGroupSettingsByGroupId().values()) {
            for (SignalGroupSettingsData lastSettings : lastPhase.getSignalGroupSettingsByGroupId().values()) {
                intergreen = settings.getOnset() - lastSettings.getDropping();
                log.error("intergreen: " + intergreen);
                if (intergreen >= 0) {
                    if ((!map.containsKey(settings)) || map.get(settings).intergreen > intergreen) {
                        ic = new IntergreenConstraint();
                        ic.onSettingsId = settings.getSignalGroupId();
                        ic.droppingSettingsId = lastSettings.getSignalGroupId();
                        ic.intergreen = intergreen;
                        map.put(settings, ic);
                    }
                }
            }
        }
        return map.values();
    }

    private List<SignalGroupSettingsData> calculateSettingsShorterThanPhase(DgPhase phase) {
        List<SignalGroupSettingsData> settingsList = new ArrayList<SignalGroupSettingsData>();
        for (SignalGroupSettingsData settings : phase.getSignalGroupSettingsByGroupId().values()) {
            if (settings.getOnset() == phase.getPhaseStartSecond() && settings.getDropping() == phase.getPhaseEndSecond()) {
                continue;
            }
            settingsList.add(settings);
        }
        return settingsList;
    }

    private void addPhaseToPlan(DgPhase p, SignalPlanData newPlan) {
        for (SignalGroupSettingsData settings : p.getSignalGroupSettingsByGroupId().values()) {
            newPlan.addSignalGroupSettings(settings);
        }
    }

    /**
	 * Calculates phases sorted by start time
	 * A signal group is considered to be member of a phase if it
	 *   - starts together with others at a time t
	 *   - starts after t but ends at the same time as the groups starting at t
	 */
    private List<DgPhase> calculateSortedPhases(final List<SignalGroupSettingsData> groupSettingsList) {
        List<DgPhase> phases = new ArrayList<DgPhase>();
        ArrayList<SignalGroupSettingsData> settingsList = new ArrayList<SignalGroupSettingsData>();
        settingsList.addAll(groupSettingsList);
        Collections.sort(settingsList, new DgSignalGroupSettingsDataOnsetComparator());
        Map<Integer, Set<SignalGroupSettingsData>> onsetSettingsMap = new HashMap<Integer, Set<SignalGroupSettingsData>>();
        Map<Integer, Set<SignalGroupSettingsData>> droppingSettingsMap = new HashMap<Integer, Set<SignalGroupSettingsData>>();
        for (SignalGroupSettingsData settings : groupSettingsList) {
            if (!onsetSettingsMap.containsKey(settings.getOnset())) {
                onsetSettingsMap.put(settings.getOnset(), new HashSet<SignalGroupSettingsData>());
            }
            onsetSettingsMap.get(settings.getOnset()).add(settings);
            if (!droppingSettingsMap.containsKey(settings.getDropping())) {
                droppingSettingsMap.put(settings.getDropping(), new HashSet<SignalGroupSettingsData>());
            }
            droppingSettingsMap.get(settings.getDropping()).add(settings);
        }
        Set<SignalGroupSettingsData> handledSettings = new HashSet<SignalGroupSettingsData>();
        for (SignalGroupSettingsData settings : settingsList) {
            if (handledSettings.contains(settings)) {
                continue;
            }
            Set<SignalGroupSettingsData> sameOnsetSettings = onsetSettingsMap.get(settings.getOnset());
            SignalGroupSettingsData lastDropSettings = this.getLastDroppingSettings(sameOnsetSettings);
            int phaseOn = lastDropSettings.getOnset();
            int phaseDrop = lastDropSettings.getDropping();
            DgPhase phase = new DgPhase(phaseOn, phaseDrop);
            phases.add(phase);
            Set<SignalGroupSettingsData> sameDroppingSettings = droppingSettingsMap.get(phaseDrop);
            for (SignalGroupSettingsData sameOnsetSetting : sameOnsetSettings) {
                if (sameOnsetSetting.getDropping() <= phaseDrop) {
                    handledSettings.add(sameOnsetSetting);
                    phase.addSignalGroupSettingsData(sameOnsetSetting);
                } else {
                    log.error("should not happen");
                }
            }
            for (SignalGroupSettingsData sameDropSetting : sameDroppingSettings) {
                if (handledSettings.contains(sameDropSetting)) {
                    continue;
                }
                if (sameDropSetting.getOnset() >= phaseOn) {
                    handledSettings.add(sameDropSetting);
                    phase.addSignalGroupSettingsData(sameDropSetting);
                } else {
                    log.error("should not happen");
                }
            }
        }
        return phases;
    }

    private SignalGroupSettingsData getLastDroppingSettings(Set<SignalGroupSettingsData> sameOnsetSettings) {
        SignalGroupSettingsData lastDropSettings = null;
        for (SignalGroupSettingsData settings : sameOnsetSettings) {
            if (settings.getOnset() > settings.getDropping()) {
                throw new IllegalStateException("onset > dropping not implemented yet!");
            }
            if (lastDropSettings == null || settings.getDropping() >= lastDropSettings.getDropping()) {
                lastDropSettings = settings;
            }
        }
        return lastDropSettings;
    }

    private Set<SignalGroupSettingsData> removeAllGreenSignalGroupSettings(List<SignalGroupSettingsData> groupSettingsList, Integer cycleTime) {
        Set<SignalGroupSettingsData> allGreenSettings = new HashSet<SignalGroupSettingsData>();
        ListIterator<SignalGroupSettingsData> it = groupSettingsList.listIterator();
        while (it.hasNext()) {
            SignalGroupSettingsData settings = it.next();
            if (settings.getOnset() == 0 && settings.getDropping() == cycleTime) {
                allGreenSettings.add(settings);
                it.remove();
            }
        }
        return allGreenSettings;
    }

    /**
	 * @param args
	 * @throws IOException 
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws JAXBException 
	 */
    public static void main(String[] args) throws JAXBException, SAXException, ParserConfigurationException, IOException {
        String signalControlFile = DgPaths.REPOS + "shared-svn/studies/dgrether/cottbus/cottbus_feb_fix/signal_control.xml";
        String signalControlOutFile = DgPaths.REPOS + "shared-svn/studies/dgrether/cottbus/cottbus_feb_fix/signal_control_sylvia.xml";
        String signalGroupsFile = DgPaths.REPOS + "shared-svn/studies/dgrether/cottbus/cottbus_feb_fix/signal_groups.xml";
        String signalGroupsOutFile = DgPaths.REPOS + "shared-svn/studies/dgrether/cottbus/cottbus_feb_fix/signal_groups_sylvia.xml";
        new DgSylviaPreprocessData().convertFixedTimePlansToSylviaBasePlans(signalControlFile, signalControlOutFile, signalGroupsFile, signalGroupsOutFile);
    }
}
