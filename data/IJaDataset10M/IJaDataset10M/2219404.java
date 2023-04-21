package TdbArchiver.Collector.spectrum;

import TdbArchiver.Collector.TdbCollector;
import TdbArchiver.Collector.TdbModeHandler;
import TdbArchiver.Collector.Tools.FileTools;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.ConnectionException;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.IStringSpectrum;
import fr.esrf.tangoatk.core.IStringSpectrumListener;
import fr.esrf.tangoatk.core.StringSpectrumEvent;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RO;

public class StringSpectrum_RO extends TdbCollector implements IStringSpectrumListener {

    /**
	 *
	 */
    private static final long serialVersionUID = -4853543068939790352L;

    public StringSpectrum_RO(TdbModeHandler _modeHandler, String currentDsPath, String currentDbPath) {
        super(_modeHandler, currentDsPath, currentDbPath);
    }

    public void addSource(AttributeLightMode attributeLightMode) throws ArchivingException {
        try {
            synchronized (attributeList) {
                IStringSpectrum attribute = (IStringSpectrum) attributeList.add(attributeLightMode.getAttribute_complete_name());
                attribute.addListener(this);
                attribute.addErrorListener(this);
                Util.out4.println("\t The attribute named " + attributeLightMode.getAttribute_complete_name() + " was hired to the Collector list...");
                String table_name = super.dbProxy.getDataBase().getDbUtil().getTableName(attributeLightMode.getAttribute_complete_name());
                FileTools myFile = new FileTools(table_name, attributeLightMode.getData_format(), attributeLightMode.getWritable(), attributeLightMode.getMode().getTdbSpec().getExportPeriod(), super.m_logger, true, super.dbProxy, super.m_currentDsPath, super.m_currentDbPath);
                myFile.initialize();
                filesNames.put(attributeLightMode.getAttribute_complete_name(), myFile);
                if (attributeList.size() == 1) {
                    startCollecting();
                }
                if (attributeList.get(attributeLightMode.getAttribute_complete_name()) == null) {
                    super.m_logger.trace(ILogger.LEVEL_WARNING, "addSource/The first add test failed for attribute|" + attributeLightMode.getAttribute_complete_name());
                }
            }
        } catch (ConnectionException e) {
            super.m_logger.trace(ILogger.LEVEL_WARNING, e);
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed adding '" + attributeLightMode.getAttribute_complete_name() + "' as source";
            String reason = GlobalConst.TANGO_COMM_EXCEPTION;
            String desc = "Failed while executing Spectrum_RO.addSource() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, "", e);
        } catch (Exception e) {
            super.m_logger.trace(ILogger.LEVEL_WARNING, "Unexpected exception during addSource:");
            super.m_logger.trace(ILogger.LEVEL_WARNING, e);
        }
    }

    public void removeSource(String attributeName) throws ArchivingException {
        System.out.println("StringSpectrum_RO.removeSource");
        try {
            synchronized (attributeList) {
                IStringSpectrum attribute = (IStringSpectrum) attributeList.get(attributeName);
                if (attribute != null) {
                    attribute.removeListener(this);
                    attribute.removeErrorListener(this);
                    this.attributeList.remove(attributeName);
                    Util.out4.println("\t The attribute named " + attributeName + " was fired from the Collector list...");
                    ((FileTools) filesNames.get(attributeName)).closeFile(true);
                    filesNames.remove(attributeName);
                    if (attributeList.isEmpty()) {
                        stopCollecting();
                    }
                }
            }
        } catch (Exception e) {
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed removing '" + attributeName + "' from sources";
            String reason = GlobalConst.TANGO_COMM_EXCEPTION;
            String desc = "Failed while executing StringSpectrum_RO.removeSource() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, "", e);
        }
    }

    public void errorChange(ErrorEvent errorEvent) {
        int tryNumber = DEFAULT_TRY_NUMBER;
        Util.out3.println("StringSpectrum_RO.errorChange : " + "Unable to read the attribute named " + errorEvent.getSource().toString());
        String[] value = null;
        SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
        spectrumEvent_ro.setAttribute_complete_name(((IStringSpectrum) errorEvent.getSource()).getName());
        spectrumEvent_ro.setTimeStamp(errorEvent.getTimeStamp());
        spectrumEvent_ro.setValue(value);
        processEventSpectrum(spectrumEvent_ro, tryNumber);
    }

    public void stringSpectrumChange(StringSpectrumEvent event) {
        int tryNumber = DEFAULT_TRY_NUMBER;
        String[] value = event.getValue();
        SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
        spectrumEvent_ro.setAttribute_complete_name(((IStringSpectrum) event.getSource()).getName());
        spectrumEvent_ro.setDim_x(value.length);
        spectrumEvent_ro.setTimeStamp(event.getTimeStamp());
        spectrumEvent_ro.setValue(value);
        processEventSpectrum(spectrumEvent_ro, tryNumber);
    }

    public void stateChange(AttributeStateEvent event) {
    }

    public void processEventSpectrum(SpectrumEvent_RO spectrumEvent_ro, int try_number) {
        Util.out4.println("StringSpectrum_RO.processEventSpectrum");
        boolean timeCondition = super.isDataArchivableTimestampWise(spectrumEvent_ro);
        if (!timeCondition) {
            return;
        }
        try {
            ((FileTools) filesNames.get(spectrumEvent_ro.getAttribute_complete_name())).processEventSpectrum(spectrumEvent_ro);
            super.setLastTimestamp(spectrumEvent_ro);
        } catch (Exception e) {
            Util.out2.println("ERROR !! " + "\r\n" + "\t Origin : \t " + "StringSpectrum_RO.processEventSpectrum" + "\r\n" + "\t Reason : \t " + e.getClass().getName() + "\r\n" + "\t Description : \t " + e.getMessage() + "\r\n" + "\t Additional information : \t " + "" + "\r\n");
            e.printStackTrace();
        }
        checkGC();
    }
}
