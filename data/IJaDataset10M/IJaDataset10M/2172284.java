package fr.soleil.mambo.tools;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.containers.view.AbstractViewSpectrumPanel;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.options.sub.VCOptions;

/**
 * A class used to save a Spectrum in a {@link File}
 * 
 * @author girardot
 */
public class SpectrumDataWriter {

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Writes the {@link String} representation of a spectrum in a {@link File}
     * 
     * @param toSave The {@link File} in which to save the spectrum representation
     * @param readData The {@link DbData} that represents the Read value of the spectrum
     * @param writeData The {@link DbData} that represents the Write value of the spectrum
     * @param selectedRead A filter for read value
     * @param selectedWrite A filter for write value
     * @param spectrumType The expected type of representation
     * @param caller The {@link Component} that called this method. This is used to display warning
     *            popups in case of problems
     */
    public static void writeDataInFile(final File toSave, final DbData readData, final DbData writeData, final List<Integer> selectedRead, final List<Integer> selectedWrite, final int spectrumType, final AbstractViewSpectrumPanel caller) {
        final int indexLengthRead = computeLimit(readData, true);
        final int timeLengthRead = computeLimit(readData, false);
        final int indexLengthWrite = computeLimit(writeData, true);
        final int timeLengthWrite = computeLimit(writeData, false);
        final int maxColumnRead, maxColumnWrite;
        if (spectrumType == ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDEX) {
            maxColumnRead = indexLengthRead;
            maxColumnWrite = indexLengthWrite;
        } else {
            maxColumnRead = timeLengthRead;
            maxColumnWrite = timeLengthWrite;
        }
        if ((maxColumnRead > 0) || (maxColumnWrite > 0)) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    String firstMessage = WaitingDialog.getFirstMessage();
                    boolean wasOpen = WaitingDialog.isInstanceVisible();
                    WaitingDialog.changeFirstMessage(Messages.getMessage("DIALOGS_FILE_WRITING"));
                    WaitingDialog.openInstance();
                    try {
                        FileWriter fw = new FileWriter(toSave);
                        Calendar calendar = Calendar.getInstance();
                        switch(spectrumType) {
                            case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDEX:
                                fw.write("Time (s)\t");
                                for (int i = 0; i < indexLengthRead; i++) {
                                    if ((selectedRead == null) || (selectedRead.contains(i))) {
                                        fw.write(Integer.toString(i + 1));
                                        fw.write(" ");
                                        fw.write(Messages.getMessage("VIEW_SPECTRUM_READ"));
                                        fw.write("\t");
                                    }
                                }
                                for (int i = 0; i < indexLengthWrite; i++) {
                                    if ((selectedWrite == null) || (selectedWrite.contains(i))) {
                                        fw.write(Integer.toString(i + 1));
                                        fw.write(" ");
                                        fw.write(Messages.getMessage("VIEW_SPECTRUM_WRITE"));
                                        fw.write("\t");
                                    }
                                }
                                break;
                            case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME:
                            case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME_STACK:
                                fw.write("Index\t");
                                for (int i = 0; i < timeLengthRead; i++) {
                                    if ((selectedRead == null) || (selectedRead.contains(i))) {
                                        if (readData.getData_timed()[i].time == null) {
                                            fw.write("null");
                                        } else {
                                            calendar.setTimeInMillis(readData.getData_timed()[i].time);
                                            fw.write(DATE_FORMAT.format(calendar.getTime()));
                                        }
                                        fw.write(" ");
                                        fw.write(Messages.getMessage("VIEW_SPECTRUM_READ"));
                                        fw.write("\t");
                                    }
                                }
                                for (int i = 0; i < timeLengthWrite; i++) {
                                    if ((selectedWrite == null) || (selectedWrite.contains(i))) {
                                        if (writeData.getData_timed()[i].time == null) {
                                            fw.write("null");
                                        } else {
                                            calendar.setTimeInMillis(writeData.getData_timed()[i].time);
                                            fw.write(DATE_FORMAT.format(calendar.getTime()));
                                        }
                                        fw.write(" ");
                                        fw.write(Messages.getMessage("VIEW_SPECTRUM_WRITE"));
                                        fw.write("\t");
                                    }
                                }
                                break;
                        }
                        fw.write("\n");
                        fw.flush();
                        switch(spectrumType) {
                            case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDEX:
                                int timeIndexRead = 0;
                                int timeIndexWrite = 0;
                                while ((timeIndexRead < timeLengthRead) || (timeIndexWrite < timeIndexWrite)) {
                                    boolean canWrite = true;
                                    long minTime;
                                    if ((timeIndexRead < timeLengthRead) && (timeIndexWrite < timeIndexWrite)) {
                                        if ((readData.getData_timed()[timeIndexRead].time == null) && (writeData.getData_timed()[timeIndexWrite].time == null)) {
                                            canWrite = false;
                                            timeIndexRead++;
                                            timeIndexWrite++;
                                        } else if (readData.getData_timed()[timeIndexRead].time == null) {
                                            appendSecondsToBufferFromMilliseconds(writeData.getData_timed()[timeIndexWrite].time, fw);
                                            fillIndexBufferWithNoValueString(fw, indexLengthRead, selectedRead);
                                            appendIndexesToBuffer(fw, writeData, timeIndexWrite, indexLengthWrite, selectedWrite);
                                            timeIndexRead++;
                                            timeIndexWrite++;
                                        } else if (writeData.getData_timed()[timeIndexWrite].time == null) {
                                            appendSecondsToBufferFromMilliseconds(readData.getData_timed()[timeIndexRead].time, fw);
                                            appendIndexesToBuffer(fw, readData, timeIndexRead, indexLengthRead, selectedRead);
                                            fillIndexBufferWithNoValueString(fw, indexLengthWrite, selectedWrite);
                                            timeIndexRead++;
                                            timeIndexWrite++;
                                        } else {
                                            minTime = Math.min(readData.getData_timed()[timeIndexRead].time.longValue(), writeData.getData_timed()[timeIndexWrite].time.longValue());
                                            appendSecondsToBufferFromMilliseconds(minTime, fw);
                                            if (minTime == readData.getData_timed()[timeIndexRead].time.longValue()) {
                                                appendIndexesToBuffer(fw, readData, timeIndexRead, indexLengthRead, selectedRead);
                                                timeIndexRead++;
                                                if (minTime == writeData.getData_timed()[timeIndexWrite].time.longValue()) {
                                                    appendIndexesToBuffer(fw, writeData, timeIndexWrite, indexLengthWrite, selectedWrite);
                                                    timeIndexWrite++;
                                                } else {
                                                    fillIndexBufferWithNoValueString(fw, indexLengthWrite, selectedWrite);
                                                }
                                            } else {
                                                fillIndexBufferWithNoValueString(fw, indexLengthRead, selectedRead);
                                                appendIndexesToBuffer(fw, writeData, timeIndexWrite, indexLengthWrite, selectedWrite);
                                                timeIndexWrite++;
                                            }
                                        }
                                    } else if (timeIndexRead < timeLengthRead) {
                                        appendSecondsToBufferFromMilliseconds(readData.getData_timed()[timeIndexRead].time, fw);
                                        appendIndexesToBuffer(fw, readData, timeIndexRead, indexLengthRead, selectedRead);
                                        fillIndexBufferWithNoValueString(fw, indexLengthWrite, selectedWrite);
                                        timeIndexRead++;
                                    } else if (timeIndexWrite < timeIndexWrite) {
                                        appendSecondsToBufferFromMilliseconds(writeData.getData_timed()[timeIndexWrite].time, fw);
                                        fillIndexBufferWithNoValueString(fw, indexLengthRead, selectedRead);
                                        appendIndexesToBuffer(fw, writeData, timeIndexWrite, indexLengthWrite, selectedWrite);
                                        timeIndexWrite++;
                                    }
                                    if (canWrite) {
                                        fw.write("\n");
                                        fw.flush();
                                    }
                                }
                                break;
                            case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME:
                            case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME_STACK:
                                int maxIndexLimit = Math.max(indexLengthRead, indexLengthWrite);
                                for (int index = 0; index < maxIndexLimit; index++) {
                                    new StringBuffer();
                                    fw.write(Double.toString(index));
                                    fw.write("\t");
                                    appendTimesToBuffer(fw, readData, timeLengthRead, index, selectedRead);
                                    appendTimesToBuffer(fw, writeData, timeLengthWrite, index, selectedWrite);
                                    fw.write("\n");
                                    fw.flush();
                                }
                                break;
                        }
                        fw.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(caller, Messages.getMessage("DIALOGS_FILE_CHOOSER_FILE_SAVING_ERROR") + e.getMessage());
                    } catch (OutOfMemoryError oome) {
                        caller.outOfMemoryErrorManagement();
                    } finally {
                        if (WaitingDialog.isInstanceVisible()) {
                            if (wasOpen) {
                                WaitingDialog.changeFirstMessage(firstMessage);
                            } else {
                                WaitingDialog.closeInstance();
                            }
                        }
                    }
                }
            });
        }
    }

    protected static int computeLimit(DbData data, boolean index) {
        int limit = 0;
        if (data != null) {
            if (index) {
                for (int timeIndex = 0; timeIndex < data.getData_timed().length; timeIndex++) {
                    if ((data.getData_timed()[timeIndex].value != null) && (data.getData_timed()[timeIndex].value.length > limit)) {
                        limit = data.getData_timed()[timeIndex].value.length;
                    }
                }
            } else {
                limit = data.getData_timed().length;
            }
        }
        return limit;
    }

    /**
     * Fills all indexes with {@link VCOptions#getNoValueString()} for an index representation of
     * spectrum, and writes this String in a {@link FileWriter}.
     * 
     * @param fw the {@link FileWriter} to write.
     * @param maxIndex the maximum index
     * @param selectedIndexes the list of selected indexes
     * @return a {@link StringBuffer} containing the expected string
     * @throws IOException if a problem occurred while file writing
     */
    protected static void fillIndexBufferWithNoValueString(FileWriter fw, int maxIndex, List<Integer> selectedIndexes) throws IOException {
        if (fw != null) {
            for (int index = 0; index < maxIndex; index++) {
                if ((selectedIndexes == null) || (selectedIndexes.contains(index))) {
                    fw.write(Options.getInstance().getVcOptions().getNoValueString());
                    fw.write("\t");
                    fw.flush();
                }
            }
        }
    }

    /**
     * Writes the index representation of a spectrum at a particular time in a {@link FileWriter}
     * 
     * @param fw the {@link FileWriter}.
     * @param data the {@link DbData} containing the spectrum information
     * @param timeIndex the index in {@link DbData} time table that corresponds to the expected time
     * @param maxIndex the maximum index
     * @param selectedIndexes the list of selected indexes
     * @return a {@link StringBuffer} containing the expected string
     * @throws IOException if a problem occurred while file writing
     */
    protected static void appendIndexesToBuffer(FileWriter fw, DbData data, int timeIndex, int maxIndex, List<Integer> selectedIndexes) throws IOException {
        if (fw != null) {
            for (int index = 0; index < maxIndex; index++) {
                if ((selectedIndexes == null) || (selectedIndexes.contains(index))) {
                    if ((data.getData_timed()[timeIndex].value != null) && (index < data.getData_timed()[timeIndex].value.length)) {
                        fw.write(String.valueOf(data.getData_timed()[timeIndex].value[index]));
                    } else {
                        fw.write(Options.getInstance().getVcOptions().getNoValueString());
                    }
                    fw.write("\t");
                    fw.flush();
                }
            }
        }
    }

    /**
     * Writes the {@link String} representation of a time in seconds, based on its value in
     * milliseconds, in a {@link FileWriter}
     * 
     * @param milli the time in milliseconds
     * @param fw the {@link FileWriter}.
     * @return a {@link StringBuffer} containing the expected string
     * @throws IOException if a problem occurred while file writing
     */
    protected static void appendSecondsToBufferFromMilliseconds(long milli, FileWriter fw) throws IOException {
        if (fw != null) {
            long ts = milli / 1000;
            long ms = milli % 1000;
            if (ms == 0) {
                fw.write(Long.toString(ts));
                fw.write("\t");
            } else if (ms < 10) {
                fw.write(Long.toString(ts));
                fw.write(".00");
                fw.write(Long.toString(ms));
                fw.write("\t");
            } else if (ms < 100) {
                fw.write(Long.toString(ts));
                fw.write(".0");
                fw.write(Long.toString(ms));
                fw.write("\t");
            } else {
                fw.write(Long.toString(ts));
                fw.write(".");
                fw.write(Long.toString(ms));
                fw.write("\t");
            }
            fw.flush();
        }
    }

    /**
     * Writes the time representation of a spectrum at a particular index in a {@link FileWriter}
     * 
     * @param fw the {@link FileWriter}.
     * @param data the {@link DbData} containing the spectrum information
     * @param timeLength The supposed length of the time table. If the real length is less than this
     *            value, the missing elements are filled with {@link VCOptions#getNoValueString()}
     * @param currentIndex The index for which the {@link String} representation is desired
     * @param selectedTimes The list of desired indexes in time
     * @return a {@link StringBuffer} containing the expected string
     * @throws IOException if a problem occurred while file writing
     */
    protected static void appendTimesToBuffer(FileWriter fw, DbData data, int timeLength, int currentIndex, List<Integer> selectedTimes) throws IOException {
        if (fw != null) {
            for (int time = 0; time < timeLength; time++) {
                if ((data.getData_timed()[time].value != null) && (currentIndex < data.getData_timed()[time].value.length) && ((selectedTimes == null) || (selectedTimes.contains(time)))) {
                    fw.write(String.valueOf(data.getData_timed()[time].value[currentIndex]));
                } else {
                    fw.write(Options.getInstance().getVcOptions().getNoValueString());
                }
                fw.write("\t");
                fw.flush();
            }
        }
    }
}
