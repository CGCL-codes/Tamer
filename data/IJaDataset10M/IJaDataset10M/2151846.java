package ru.cos.sim.meters.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jdom.Element;
import org.jdom.Namespace;
import ru.cos.cs.agents.framework.exceptions.SimulationEngineRuntimeException;
import ru.cos.sim.mdf.MDFReader;
import ru.cos.sim.meters.data.DensityFlowMeterInitData;
import ru.cos.sim.meters.data.DensityMeterInitData;
import ru.cos.sim.meters.data.FlowMeterInitData;
import ru.cos.sim.meters.data.LinkAverageTravelSpeedMeterInitData;
import ru.cos.sim.meters.data.MeterData;
import ru.cos.sim.meters.data.SATSMeterInitData;
import ru.cos.sim.meters.data.TrafficVoulmeMeterInitData;
import ru.cos.sim.meters.data.VAHMeterInitData;
import ru.cos.sim.meters.framework.ModesInitData;
import ru.cos.sim.meters.framework.TimePeriod;
import ru.cos.sim.meters.impl.MeterType;

/**
 * 
 * @author zroslaw
 */
public class MetersDataReader {

    public static final Namespace NS = MDFReader.MDF_NAMESPACE;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String DATA_COLLECTION_MODES = "DataCollectionModes";

    public static final String AVERAGE_DATA_MODE = "AverageDataMode";

    public static final String HISTORY_DATA_MODE = "HistoryDataMode";

    public static final String LOG_INTERVAL = "logInterval";

    public static final String SCHEDULED_AVG_DATA_MODE = "ScheduledAverageDataMode";

    public static final String SCHEDULE = "Schedule";

    public static final String SCHEDULED_HISTORY_DATA_MODE = "ScheduledHistoryDataMode";

    public static final String TIME_PERIOD = "TimePeriod";

    public static final String FROM = "from";

    public static final String TO = "to";

    public static final String LINK_ID = "linkId";

    public static final String SEGMENT_ID = "segmentId";

    public static final String POSITION = "position";

    public static final String LENGTH = "length";

    public static final String SECTION_LENGTH = "sectionLength";

    public static final String MEASURING_TIME = "measuringTime";

    public static Set<MeterData> read(Element metersElement) {
        Set<MeterData> meterDataSet = new HashSet<MeterData>();
        if (metersElement == null) return meterDataSet;
        for (Object meterObj : metersElement.getChildren()) {
            MeterData meterData;
            Element meterElement = (Element) meterObj;
            MeterType meterType = MeterType.valueOf(meterElement.getName());
            switch(meterType) {
                case TrafficVolumeMeter:
                    {
                        int linkId = Integer.parseInt(meterElement.getChildText(LINK_ID, NS));
                        int segmentId = Integer.parseInt(meterElement.getChildText(SEGMENT_ID, NS));
                        float position = Float.parseFloat(meterElement.getChildText(POSITION, NS));
                        TrafficVoulmeMeterInitData tvm = new TrafficVoulmeMeterInitData();
                        tvm.setLinkId(linkId);
                        tvm.setSegmentId(segmentId);
                        tvm.setPosition(position);
                        meterData = tvm;
                        break;
                    }
                case InstantAverageSpeedMeter:
                case TotalTravelTimeMeter:
                case AverageTravelTimeMeter:
                case NetworkAverageTravelSpeedMeter:
                    {
                        meterData = new MeterData();
                        break;
                    }
                case LinkAverageTravelSpeedMeter:
                    {
                        int linkId = Integer.parseInt(meterElement.getChildText(LINK_ID, NS));
                        LinkAverageTravelSpeedMeterInitData linkAverageTravelSpeedMeterInitData = new LinkAverageTravelSpeedMeterInitData();
                        linkAverageTravelSpeedMeterInitData.setLinkId(linkId);
                        meterData = linkAverageTravelSpeedMeterInitData;
                        break;
                    }
                case FlowMeter:
                    {
                        int linkId = Integer.parseInt(meterElement.getChildText(LINK_ID, NS));
                        int segmentId = Integer.parseInt(meterElement.getChildText(SEGMENT_ID, NS));
                        float position = Float.parseFloat(meterElement.getChildText(POSITION, NS));
                        float measuringTime = Float.parseFloat(meterElement.getChildText(MEASURING_TIME, NS));
                        FlowMeterInitData fmid = new FlowMeterInitData();
                        fmid.setLinkId(linkId);
                        fmid.setSegmentId(segmentId);
                        fmid.setPosition(position);
                        fmid.setMeasuringTime(measuringTime);
                        meterData = fmid;
                        break;
                    }
                case DensityMeter:
                    {
                        int linkId = Integer.parseInt(meterElement.getChildText(LINK_ID, NS));
                        int segmentId = Integer.parseInt(meterElement.getChildText(SEGMENT_ID, NS));
                        float position = Float.parseFloat(meterElement.getChildText(POSITION, NS));
                        float length = Float.parseFloat(meterElement.getChildText(SECTION_LENGTH, NS));
                        DensityMeterInitData dmid = new DensityMeterInitData();
                        dmid.setLinkId(linkId);
                        dmid.setSegmentId(segmentId);
                        dmid.setPosition(position);
                        dmid.setLength(length);
                        meterData = dmid;
                        break;
                    }
                case DensityFlowMeter:
                    {
                        int linkId = Integer.parseInt(meterElement.getChildText(LINK_ID, NS));
                        int segmentId = Integer.parseInt(meterElement.getChildText(SEGMENT_ID, NS));
                        float position = Float.parseFloat(meterElement.getChildText(POSITION, NS));
                        float length = Float.parseFloat(meterElement.getChildText(LENGTH, NS));
                        float measuringTime = Float.parseFloat(meterElement.getChildText(MEASURING_TIME, NS));
                        DensityFlowMeterInitData dfmid = new DensityFlowMeterInitData();
                        DensityMeterInitData dmid = new DensityMeterInitData();
                        dmid.setLinkId(linkId);
                        dmid.setSegmentId(segmentId);
                        dmid.setPosition(position);
                        dmid.setLength(length);
                        FlowMeterInitData fmid = new FlowMeterInitData();
                        fmid.setLinkId(linkId);
                        fmid.setSegmentId(segmentId);
                        fmid.setPosition(position);
                        fmid.setMeasuringTime(measuringTime);
                        dfmid.setDensityMeterInitData(dmid);
                        dfmid.setFlowMeterInitData(fmid);
                        meterData = dfmid;
                        break;
                    }
                case VehiclesAppearanceHeadwayMeter:
                    {
                        VAHMeterInitData him = new VAHMeterInitData();
                        int linkId = Integer.parseInt(meterElement.getChildText(LINK_ID, NS));
                        int segmentId = Integer.parseInt(meterElement.getChildText(SEGMENT_ID, NS));
                        float position = Float.parseFloat(meterElement.getChildText(POSITION, NS));
                        float timeBin = Float.parseFloat(meterElement.getChildText("timeBinLength"));
                        him.setLinkId(linkId);
                        him.setSegmentId(segmentId);
                        him.setPosition(position);
                        him.setTimeBin(timeBin);
                        meterData = him;
                        break;
                    }
                case SectionAverageTravelSpeedMeter:
                    {
                        int linkId = Integer.parseInt(meterElement.getChildText(LINK_ID, NS));
                        int segmentId = Integer.parseInt(meterElement.getChildText(SEGMENT_ID, NS));
                        float position = Float.parseFloat(meterElement.getChildText(POSITION, NS));
                        float length = Float.parseFloat(meterElement.getChildText(SECTION_LENGTH, NS));
                        SATSMeterInitData sid = new SATSMeterInitData();
                        sid.setLinkId(linkId);
                        sid.setSegmentId(segmentId);
                        sid.setPosition(position);
                        sid.setLength(length);
                        meterData = sid;
                        break;
                    }
                default:
                    throw new SimulationEngineRuntimeException("Unknown type of meter: " + meterType);
            }
            int meterId = Integer.parseInt(meterElement.getChildText(ID, NS));
            String name = meterElement.getChildText(NAME, NS);
            ModesInitData modesInitData = initMeterModes(meterElement.getChild(DATA_COLLECTION_MODES, NS));
            meterData.setId(meterId);
            meterData.setType(meterType);
            meterData.setName(name);
            meterData.setModesInitData(modesInitData);
            meterDataSet.add(meterData);
        }
        return meterDataSet;
    }

    private static ModesInitData initMeterModes(Element dataCollectionModes) {
        ModesInitData modesInitData = new ModesInitData();
        if (dataCollectionModes == null) return modesInitData;
        if (dataCollectionModes.getChild(AVERAGE_DATA_MODE, NS) != null) modesInitData.setAverageDataMeasured(true);
        Element historyDataMode = dataCollectionModes.getChild(HISTORY_DATA_MODE, NS);
        if (historyDataMode != null) {
            modesInitData.setHistoryMeasured(true);
            Element historyLogIntervalElement = historyDataMode.getChild(LOG_INTERVAL, NS);
            if (historyLogIntervalElement != null) {
                float historyLogInterval = Float.parseFloat(historyLogIntervalElement.getText());
                modesInitData.setHistoryLogInterval(historyLogInterval);
            }
        }
        Element scheduledAverageDataMode = dataCollectionModes.getChild(SCHEDULED_AVG_DATA_MODE, NS);
        if (scheduledAverageDataMode != null) {
            List<TimePeriod> schedule = initMeterSchedule(scheduledAverageDataMode.getChild(SCHEDULE, NS));
            modesInitData.setAverageDataCollectorSchedule(schedule);
        }
        Element scheduledHistoryDataMode = dataCollectionModes.getChild(SCHEDULED_HISTORY_DATA_MODE, NS);
        if (scheduledHistoryDataMode != null) {
            List<TimePeriod> schedule = initMeterSchedule(scheduledHistoryDataMode.getChild(SCHEDULE, NS));
            modesInitData.setHistoryDataCollectorSchedule(schedule);
            Element historyLogIntervalElement = scheduledHistoryDataMode.getChild(LOG_INTERVAL, NS);
            if (historyLogIntervalElement != null) {
                float historyLogInterval = Float.parseFloat(historyLogIntervalElement.getText());
                modesInitData.setScheduledHistoryLogInterval(historyLogInterval);
            }
        }
        return modesInitData;
    }

    private static List<TimePeriod> initMeterSchedule(Element scheduleElement) {
        List<TimePeriod> result = new ArrayList<TimePeriod>();
        List<Element> timePeroiodElements = scheduleElement.getChildren(TIME_PERIOD, NS);
        for (Element timePeriodElement : timePeroiodElements) {
            float timeFrom = Float.parseFloat(timePeriodElement.getChildText(FROM, NS));
            float timeTo = Float.parseFloat(timePeriodElement.getChildText(TO, NS));
            TimePeriod timePeriod = new TimePeriod(timeFrom, timeTo);
            result.add(timePeriod);
        }
        return result;
    }
}
