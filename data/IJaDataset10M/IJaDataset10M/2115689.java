package org.scrinch.model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.scrinch.model.castor.ScrinchEnv;
import org.scrinch.model.castor.Preferences;

public class ScrinchEnvToolkit {

    public static final int DEFAULT_MEAN_VEOLCITY = 10;

    public static final String DEFAULT_REPORT_ENCODING = "UTF-8";

    public static Logger logger;

    private boolean burnUpChart = true;

    private ScrinchEnv lastSrinchData = null;

    private ItemFactory itemFactory;

    private MemberFactory memberFactory;

    private OriginTypeFactory originTypeFactory;

    private ProjectFactory projectFactory;

    private SprintFactory sprintFactory;

    private TargetFactory targetFactory;

    private WorkTypeFactory workTypeFactory;

    private boolean oldProjectsAndSprintsVisible;

    private boolean useItemSetScrollPane = true;

    public interface Listener {

        void preferencesChanged();

        void dataLoaded();

        void dataChanged();
    }

    private Collection<Listener> listeners = new Vector<Listener>();

    public ScrinchEnvToolkit() {
        initFactories();
    }

    public synchronized void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public synchronized void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public void preferencesChanged() {
        Listener[] listeners = this.listeners.toArray(new Listener[0]);
        for (Listener listener : listeners) {
            try {
                listener.preferencesChanged();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Error notifying listener of preferences changed", ex);
            }
        }
    }

    public void dataChanged() {
        Listener[] listeners = this.listeners.toArray(new Listener[0]);
        for (Listener listener : listeners) {
            try {
                listener.dataChanged();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Error notifying listener of data changed", ex);
            }
        }
    }

    public void dataLoaded() {
        Listener[] listeners = this.listeners.toArray(new Listener[0]);
        for (Listener listener : listeners) {
            try {
                listener.dataLoaded();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Error notifying listener of data loaded", ex);
            }
        }
    }

    public void showAllSprints() {
        oldProjectsAndSprintsVisible = true;
    }

    public void setOldProjectsAndSprintsVisible(boolean visible) {
        oldProjectsAndSprintsVisible = visible;
        preferencesChanged();
    }

    public boolean isOldProjectsAndSprintsVisible() {
        return oldProjectsAndSprintsVisible;
    }

    private void initFactories() {
        this.itemFactory = new ItemFactory(this);
        this.originTypeFactory = new OriginTypeFactory(this);
        this.projectFactory = new ProjectFactory(this);
        this.memberFactory = new MemberFactory(this);
        this.sprintFactory = new SprintFactory(this);
        this.targetFactory = new TargetFactory(this);
        this.workTypeFactory = new WorkTypeFactory(this);
    }

    public TargetFactory getTargetFactory() {
        return this.targetFactory;
    }

    public WorkTypeFactory getWorkTypeFactory() {
        return this.workTypeFactory;
    }

    public SprintFactory getSprintFactory() {
        return this.sprintFactory;
    }

    public ProjectFactory getProjectFactory() {
        return this.projectFactory;
    }

    public OriginTypeFactory getOriginTypeFactory() {
        return this.originTypeFactory;
    }

    public ItemFactory getItemFactory() {
        return this.itemFactory;
    }

    public MemberFactory getMemberFactory() {
        return this.memberFactory;
    }

    public void setBurnUpChart(boolean burnUpChart) {
        this.burnUpChart = burnUpChart;
        preferencesChanged();
    }

    public boolean useItemSetScrollPane() {
        return this.useItemSetScrollPane;
    }

    public void setUseItemSetScrollPane(boolean useItemSetScrollPane) {
        this.useItemSetScrollPane = useItemSetScrollPane;
    }

    public boolean isBurnUpChart() {
        return this.burnUpChart;
    }

    public double getMeanVelocityWithin(boolean correctedAccordingToSlowDowns, Date start, Date end) {
        Double meanVelocity = sprintFactory.getMeanVelocity(correctedAccordingToSlowDowns, start, end);
        if (meanVelocity == null) {
            meanVelocity = new Double(DEFAULT_MEAN_VEOLCITY);
        }
        return meanVelocity.doubleValue();
    }

    public double getMeanVelocity() {
        return getMeanVelocity(false);
    }

    public double getMeanVelocity(boolean correctedAccordingToSlowDowns) {
        Calendar calendar = getPreparedCalendarInstance();
        calendar.setTime(new Date());
        Date today = calendar.getTime();
        calendar.add(Calendar.WEEK_OF_YEAR, -8);
        Date eightWeeksAgo = calendar.getTime();
        return getMeanVelocityWithin(correctedAccordingToSlowDowns, eightWeeksAgo, today);
    }

    public boolean isWeekDay(Date date) {
        Calendar calendar = getPreparedCalendarInstance();
        calendar.setTime(date);
        return (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY);
    }

    public int getBusinessDaysCount(Date startDate, Date endDate) {
        int businessDaysCount = 0;
        Calendar calendar = getPreparedCalendarInstance();
        if (startDate != null && endDate != null && !startDate.after(endDate)) {
            Date iter = startDate;
            while (!iter.after(endDate)) {
                if (isWeekDay(iter)) {
                    businessDaysCount++;
                }
                calendar.setTime(iter);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                iter = calendar.getTime();
            }
        }
        return businessDaysCount;
    }

    public Date getNextWeekDayIfNotWeekDay(Date date) {
        Calendar calendar = getPreparedCalendarInstance();
        calendar.setTime(date);
        while (!isWeekDay(calendar.getTime())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar.getTime();
    }

    public Date getLatestDateAdjustedIfNotWeekDay(Date firstDate, Date lastDate) {
        Date correctedEndDate = firstDate;
        if (lastDate != null) {
            Calendar calendar = getPreparedCalendarInstance();
            if (lastDate.before(firstDate)) {
                calendar.setTime(firstDate);
            } else {
                calendar.setTime(lastDate);
            }
            correctedEndDate = getNextWeekDayIfNotWeekDay(calendar.getTime());
        }
        return correctedEndDate;
    }

    public Date getPreviousWeekDayIfNotWeekDay(Date date) {
        Calendar calendar = getPreparedCalendarInstance();
        calendar.setTime(date);
        while (!isWeekDay(calendar.getTime())) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        return calendar.getTime();
    }

    public Date getCurrentDate() {
        Calendar calendar = getPreparedCalendarInstance();
        return calendar.getTime();
    }

    private Calendar preparedCalendar = null;

    public Calendar getPreparedCalendarInstance() {
        Calendar cal = Calendar.getInstance();
        if (preparedCalendar != null) {
            cal.set(Calendar.YEAR, preparedCalendar.get(Calendar.YEAR));
            cal.set(Calendar.MONTH, preparedCalendar.get(Calendar.MONTH));
            cal.set(Calendar.DAY_OF_MONTH, preparedCalendar.get(Calendar.DAY_OF_MONTH));
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public void setPreparedCalendarInstance(Calendar cal) {
        preparedCalendar = cal;
    }

    public static synchronized void initDefaultLogger() {
        if (logger == null) {
            logger = Logger.getLogger("scrinch");
        }
    }

    public static synchronized void initFileLogger() {
        if (logger == null) {
            try {
                logger = Logger.getLogger("scrinch");
                for (Handler handler : logger.getHandlers()) {
                    logger.removeHandler(handler);
                }
                Formatter formatter = new java.util.logging.Formatter() {

                    private final SimpleDateFormat SD = new SimpleDateFormat("yyyyMMdd HH:ss:SSS");

                    private final String lineSeparator = System.getProperty("line.separator");

                    @Override
                    public synchronized String format(LogRecord record) {
                        StringBuilder log = new StringBuilder();
                        log.append(record.getSequenceNumber());
                        log.append(" ").append(SD.format(new Date(record.getMillis())));
                        log.append(" ").append(record.getLevel());
                        log.append(" - ").append(record.getMessage()).append(lineSeparator);
                        Throwable t = record.getThrown();
                        if (t != null) {
                            StringWriter wr = new StringWriter();
                            PrintWriter pw = new PrintWriter(wr);
                            t.printStackTrace(pw);
                            pw.close();
                            log.append(wr.getBuffer());
                            log.append(record.getMessage()).append(lineSeparator);
                        }
                        return log.toString();
                    }
                };
                FileHandler handler = new FileHandler("scrinch.log", 5000000, 1, true);
                handler.setFormatter(formatter);
                logger.addHandler(handler);
            } catch (Exception e) {
                System.out.println("Logger could not be initialized... logs will not be handled in a file");
                e.printStackTrace();
            }
        }
    }

    public static void exit(int code) {
        Logger.getLogger("scrinch").info("Scrinch terminated " + (code == 0 ? "normally" : "due to an error"));
        System.exit(code);
    }

    public boolean isSomethingToSave() {
        return !memberFactory.getMemberList().isEmpty() || !itemFactory.getItemList().isEmpty() || !projectFactory.getList().isEmpty() || !sprintFactory.getSprintList().isEmpty();
    }

    private ScrinchEnv parseFile(File file) throws IOException, MarshalException, ValidationException {
        ScrinchEnv scrinchData = null;
        FileReader fileReader = new FileReader(file);
        try {
            scrinchData = (ScrinchEnv) Unmarshaller.unmarshal(ScrinchEnv.class, fileReader);
        } finally {
            fileReader.close();
        }
        return scrinchData;
    }

    private ScrinchEnv parseFile(byte[] data) throws IOException, MarshalException, ValidationException {
        ScrinchEnv scrinchData = null;
        ByteArrayInputStream brIn = new ByteArrayInputStream(data);
        InputStreamReader reader = new InputStreamReader(brIn, "ISO-8859-1");
        scrinchData = (ScrinchEnv) Unmarshaller.unmarshal(ScrinchEnv.class, reader);
        reader.close();
        brIn.close();
        return scrinchData;
    }

    private void buildModel(ScrinchEnv data) throws ScrinchException, ScrinchException, UnsupportedEncodingException {
        initFactories();
        logger.log(Level.INFO, "Loading work types");
        if (data.getWorkTypes() != null) {
            workTypeFactory.loadFromCastor(data.getWorkTypes().getWorkType());
            logger.log(Level.INFO, data.getWorkTypes().getWorkTypeCount() + " work types loaded");
        }
        logger.log(Level.INFO, "Loading origin types");
        if (data.getOriginTypes() != null) {
            originTypeFactory.loadFromCastor(data.getOriginTypes().getOriginType());
            logger.log(Level.INFO, data.getOriginTypes().getOriginTypeCount() + " origin types loaded");
        }
        logger.log(Level.INFO, "Loading members");
        if (data.getPeople() != null) {
            logger.log(Level.INFO, "Loading deprecated people struct");
            memberFactory.loadFromCastor(data.getPeople().getMember());
            logger.log(Level.INFO, data.getPeople().getMemberCount() + " members loaded");
        } else if (data.getMembers() != null) {
            memberFactory.loadFromCastor(data.getMembers().getMember());
            logger.log(Level.INFO, data.getMembers().getMemberCount() + " members loaded");
        }
        logger.log(Level.INFO, "Loading targets");
        if (data.getTargets() != null) {
            targetFactory.loadFromCastor(data.getTargets());
            logger.log(Level.INFO, data.getTargets().getTargetCount() + " targets loaded");
        }
        logger.log(Level.INFO, "Loading items");
        if (data.getItems() != null) {
            this.itemFactory.loadFromCastor(data.getItems());
            logger.log(Level.INFO, data.getItems().getItemCount() + " items loaded");
        }
        logger.log(Level.INFO, "Loading projects");
        if (data.getBacklogs() != null) {
            projectFactory.loadFromCastor(data.getBacklogs());
            logger.log(Level.INFO, data.getBacklogs().getProductBacklogCount() + " projects loaded");
        }
        logger.log(Level.INFO, "Loading sprints");
        if (data.getSprints() != null) {
            sprintFactory.loadFromCastor(data.getSprints());
            logger.log(Level.INFO, data.getSprints().getSprintCount() + " sprints loaded");
        }
        logger.log(Level.INFO, "Loading preferences");
        if (data.getPreferences() != null) {
            burnUpChart = data.getPreferences().getBurnUpChart();
        }
        dataLoaded();
    }

    private ScrinchEnv getCurrentScrinchData() throws UnsupportedEncodingException {
        Collection<Item> orphans = ItemToolkit.findOrphanItems(this);
        for (Item orphan : orphans) {
            this.itemFactory.dispose(orphan);
        }
        ScrinchEnv scrinchData = new ScrinchEnv();
        scrinchData.setWorkTypes(workTypeFactory.toCastor());
        scrinchData.setOriginTypes(originTypeFactory.toCastor());
        scrinchData.setMembers(memberFactory.toCastor());
        scrinchData.setTargets(targetFactory.toCastor());
        scrinchData.setItems(this.itemFactory.toCastor());
        scrinchData.setBacklogs(projectFactory.toCastor());
        scrinchData.setSprints(sprintFactory.toCastor());
        Preferences preferences = new Preferences();
        preferences.setBurnUpChart(burnUpChart);
        scrinchData.setPreferences(preferences);
        return scrinchData;
    }

    public boolean isFileSaveNeeded() throws UnsupportedEncodingException {
        boolean r = true;
        if (this.lastSrinchData != null) {
            ScrinchEnv scrinchData = getCurrentScrinchData();
            r = !scrinchData.equals(this.lastSrinchData);
            if (!r) {
                logger.log(Level.SEVERE, "No need to save file");
            }
        }
        return r;
    }

    public void save(File file) throws IOException, MarshalException, ValidationException {
        ScrinchEnv data = getCurrentScrinchData();
        File tempFile = new File(file.getParentFile(), file.getName() + ".temp");
        try {
            OutputStream out = new FileOutputStream(tempFile);
            if (saveData(data, out)) {
                if (file.exists()) {
                    File oldFile = new File(file.getParentFile(), file.getName() + ".old");
                    boolean canRename = true;
                    if (oldFile.exists()) {
                        canRename = oldFile.delete();
                    }
                    if (canRename) {
                        file.renameTo(oldFile);
                    } else {
                        logger.log(Level.WARNING, "Could not rename file to old file : " + "current old file could not be deleted. " + "Operation skipped");
                    }
                }
                tempFile.renameTo(file);
                this.lastSrinchData = data;
            }
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Unable to save file", e);
            tempFile.delete();
        }
    }

    public void save(OutputStream out) throws UnsupportedEncodingException {
        ScrinchEnv data = getCurrentScrinchData();
        if (saveData(data, out)) {
            this.lastSrinchData = data;
        }
    }

    private boolean saveData(ScrinchEnv data, OutputStream out) {
        try {
            Writer writer = new OutputStreamWriter(out);
            try {
                Marshaller marschaller = new Marshaller(writer);
                marschaller.marshal(data);
            } finally {
                writer.close();
            }
            return true;
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Unable to save file", e);
        }
        return false;
    }

    public void open(File file) throws IOException, MarshalException, ValidationException, ScrinchException, ScrinchException {
        logger.log(Level.INFO, "Parsing file");
        ScrinchEnv scrinchData = parseFile(file);
        logger.log(Level.INFO, "Building model");
        buildModel(scrinchData);
        this.lastSrinchData = scrinchData;
    }

    public void open(byte[] data) throws IOException, MarshalException, ValidationException, ScrinchException, ScrinchException {
        logger.log(Level.INFO, "Parsing data");
        ScrinchEnv scrinchData = parseFile(data);
        logger.log(Level.INFO, "Building model");
        buildModel(scrinchData);
        this.lastSrinchData = scrinchData;
    }
}
