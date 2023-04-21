package ru.adv.repository;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import org.springframework.jdbc.core.RowCallbackHandler;
import ru.adv.db.adapter.Types;
import ru.adv.db.base.DBCastException;
import ru.adv.db.base.DBValue;
import ru.adv.db.base.FileValue;
import ru.adv.db.base.MAttribute;
import ru.adv.db.base.MObject;
import ru.adv.db.config.ConfigObject;
import ru.adv.db.config.DBConfig;
import ru.adv.db.config.DBConfigException;
import ru.adv.db.handler.Handler;
import ru.adv.db.handler.HandlerException;
import ru.adv.db.handler.SQLSelect;
import ru.adv.db.handler.SQLStatementException;
import ru.adv.db.handler.SelectAttributes;
import ru.adv.db.handler.SelectAttributesItem;
import ru.adv.db.handler.SelectOptions;
import ru.adv.db.handler.Sort;
import ru.adv.io.InputOutputException;
import ru.adv.repository.dump.AttributeCollection;
import ru.adv.repository.dump.DumpHeader;
import ru.adv.repository.dump.DumpInfo;
import ru.adv.repository.dump.DumpInfoImpl;
import ru.adv.repository.dump.DumpObjectHeader;
import ru.adv.repository.dump.DumpRow;
import ru.adv.repository.dump.DumpWriter;
import ru.adv.repository.dump.ObjectInfoImpl;
import ru.adv.repository.dump.TypeAttr;
import ru.adv.util.ErrorCodeException;
import ru.adv.util.ProgressCallback;
import ru.adv.util.Stream;
import ru.adv.util.UnreachableCodeReachedException;

/**
 * Дампит БД и востанавливает в репозитории $Id: Dumper.java 1106 2009-06-03 07:32:17Z vic $Name: User: vic Date:
 * 09.10.2003 Time: 16:35:56
 */
public abstract class Dumper {

    private String _repositoryName;

    private Handler _handler;

    private Long _startSystemSequence = null;

    private Integer _incrementSystemSequence = null;

    private Set _criticalObjectNames;

    private AttributeCollection _fileHashAttrCollection = null;

    private FileComparator _fileComparator = null;

    private boolean _isIncludeSystemObjects = true;

    private Set _excludeObjectsNames;

    private boolean _isGenerateDumpInfo = false;

    protected Set _objectInfo = null;

    private static DBValue NULL_DIFF_DBVALUE;

    private File _tmpDir;

    static {
        try {
            NULL_DIFF_DBVALUE = DBValue.createInstance(DBValue.INT, new Integer(DumpHeader.DIFF_ACTION_UNDEFINED));
        } catch (DBCastException e) {
        }
    }

    /**
     * 
     * @param repositoryName
     * @param openedHandler
     */
    protected Dumper(String repositoryName, Handler openedHandler, File tmpDir) {
        _repositoryName = repositoryName;
        _handler = openedHandler;
        _excludeObjectsNames = Collections.EMPTY_SET;
        _tmpDir = tmpDir;
        _criticalObjectNames = Collections.EMPTY_SET;
    }

    public String getRepositoryName() {
        return _repositoryName;
    }

    protected DBConfig getDbConfig() {
        return getHandler().getDBConfig();
    }

    protected Handler getHandler() {
        return _handler;
    }

    protected Integer getIncrementSystemSequence() {
        return _incrementSystemSequence;
    }

    protected void setIncrementSystemSequence(Integer incrementSystemSequence) {
        _incrementSystemSequence = incrementSystemSequence;
    }

    protected Long getStartSystemSequence() {
        return _startSystemSequence;
    }

    protected void setStartSystemSequence(Long startSystemSequence) {
        _startSystemSequence = startSystemSequence;
    }

    public boolean isIncludeSystemObjects() {
        return _isIncludeSystemObjects;
    }

    public Set getCriticalObjectNames() {
        return _criticalObjectNames;
    }

    public void setCriticalObjectNames(Set criticalObjectNames) {
        _criticalObjectNames = Collections.unmodifiableSet(criticalObjectNames);
    }

    /**
     * В созданный дамп будет вставлена информация о содержании дампа
     * {@link ru.adv.test.repository.dump.test.DumpInfoTestImpl}
     * 
     * @see #getDumpInfo
     * @param isGenerateDumpInfo
     */
    public void setIsGenerateDumpInfo(boolean isGenerateDumpInfo) {
        _isGenerateDumpInfo = isGenerateDumpInfo;
    }

    public boolean isGenerateDumpInfo() {
        return _isGenerateDumpInfo;
    }

    /**
     * Return {@link DumpInfo} that was generated in {@link #makeDump}
     * 
     * @return may be null
     */
    public DumpInfo getDumpInfo() {
        if (_objectInfo != null && _fileHashAttrCollection != null) {
            return new DumpInfoImpl(_objectInfo, _fileHashAttrCollection.getNewObjects(), _fileHashAttrCollection.getChangedObjects(), _fileHashAttrCollection.getDeletedObjects());
        }
        return null;
    }

    protected void setIncludeSystemObjects(boolean includeSystemObjects) {
        _isIncludeSystemObjects = includeSystemObjects;
    }

    protected Set getExcludeObjectsNames() {
        return _excludeObjectsNames;
    }

    protected void setExcludeObjectNames(Set excludeObjectsNames) {
        _excludeObjectsNames = Collections.unmodifiableSet(excludeObjectsNames);
    }

    /**
     * При установке данного свойства метод {@link #makeDump} будет помещать в
     * дамп тела только тех файлов(BLOB), значения которых не найдены в
     * {@link AttributeCollection}
     * 
     * @param fileHashAttrCollection
     */
    public void setFileHashAttrCollection(AttributeCollection fileHashAttrCollection) {
        _fileHashAttrCollection = fileHashAttrCollection;
    }

    public AttributeCollection getFileHashAttrCollection() {
        return _fileHashAttrCollection;
    }

    /**
     * Дампит БД в указанный файл
     * 
     * @param dumpFilePath
     * @param configFilePath
     * @param isFiles
     *            включать ли содержимое файлов в дамп
     * @throws Exception
     * @see #setFileHashAttrCollection
     */
    protected abstract void makeDump(String dumpFilePath, String configFilePath, boolean isFiles, ProgressCallback callback) throws Exception;

    protected boolean isObjectNeedToDump(ConfigObject cObject) {
        if ((!isIncludeSystemObjects()) && cObject.getDBConfig().getDBAdapter().getSystemObjects().contains(cObject.getName())) {
            return false;
        }
        if (getExcludeObjectsNames().contains(cObject.getName())) {
            return false;
        }
        if (cObject.isSequence() && !isIncludeSystemObjects()) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет что одновременно установлены или не установлены start &
     * increment system sequence
     * 
     * @return true если установлены
     */
    protected boolean checkCustomSequence() throws UnreachableCodeReachedException {
        if (getStartSystemSequence() == null && getIncrementSystemSequence() != null) {
            throw new UnreachableCodeReachedException("Dumper bad usage: getStartSystemSequence() is not set");
        }
        if (getStartSystemSequence() != null && getIncrementSystemSequence() == null) {
            throw new UnreachableCodeReachedException("Dumper bad usage: getIncrementSystemSequence() is not set");
        }
        return getStartSystemSequence() != null && getIncrementSystemSequence() != null;
    }

    /**
     * Записывает вспомогательную таблицу деревянного объекта
     * 
     * @param dump
     * @param cObject
     */
    protected void writeExtendedTreeTable(final DumpWriter dump, ConfigObject cObject) throws ErrorCodeException, IOException {
        final DumpObjectHeader oHeader = new DumpObjectHeader(cObject.getExtendedTreeTableName());
        oHeader.addAttr(new TypeAttr(ConfigObject.TREE_EX_FIELD_ANC, Types.LONG));
        oHeader.addAttr(new TypeAttr(ConfigObject.TREE_EX_FIELD_ID, Types.LONG));
        dump.writeObjectHeader(oHeader);
        try {
            getHandler().getConnection().executeQuery(createSelect(getHandler().getDBConfig(), oHeader), null, new RowCallbackHandler() {

                public void processRow(ResultSet rs) throws SQLException {
                    DumpRow dumpRow = new DumpRow();
                    int pos = 1;
                    for (TypeAttr attr : oHeader.getTypeAttrs()) {
                        DBValue dbValue = DBValue.createInstance(attr.getType(), rs, pos++);
                        dumpRow.addValue(dbValue);
                    }
                    try {
                        dump.writeRow(dumpRow, getTmpDir());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            throw new HandlerException(HandlerException.DB_CANNOT_READ_TREE, e, getHandler().getDBConfig().getId());
        }
    }

    /**
     * Создает строку с SELECT для выбора всех значений из таблицы, атрибуты
     * таблицы выбираются из objectHeader
     * 
     * @param config
     * @param objectHeader
     * @return
     */
    private String createSelect(DBConfig config, DumpObjectHeader objectHeader) {
        SQLSelect select = new SQLSelect(config, null, false);
        String sql = "SELECT DISTINCT ";
        for (Iterator i = objectHeader.getTypeAttrs().iterator(); i.hasNext(); ) {
            TypeAttr attr = (TypeAttr) i.next();
            sql += select.sqlField(attr.getAttrName());
            if (i.hasNext()) {
                sql += ",";
            }
        }
        sql += " FROM " + select.sqlTable(objectHeader.getObjectName());
        return sql;
    }

    /**
     * @param dump
     *            куда пишем
     * @param sequenceName
     */
    protected void writeSequence(DumpWriter dump, String sequenceName) throws DBCastException, HandlerException, IOException {
        int increment = 0;
        Long currId = null;
        increment = getIncrement(sequenceName);
        currId = getCurrId(sequenceName);
        writeSequence(dump, sequenceName, currId, new Integer(increment));
    }

    private Long getCurrId(String sequenceName) throws HandlerException {
        try {
            return new Long(getHandler().getConnection().getNextID(getHandler().getDBConfig().getSchemaName(), sequenceName).getLongId().intValue());
        } catch (Throwable e) {
            throw new HandlerException(HandlerException.DB_CANNOT_GET_CURRENT_ID, e, getHandler().getDBConfig().getId());
        }
    }

    private int getIncrement(String sequenceName) throws HandlerException {
        try {
            return getHandler().getConnection().getIncrementID(getHandler().getDBConfig().getSchemaName(), sequenceName);
        } catch (SQLException e) {
            throw new HandlerException(HandlerException.DB_CANNOT_INCREMENT_SEQUENCE, e, getHandler().getDBConfig().getId());
        }
    }

    /**
     * @param dump
     *            куда пишем
     * @param sequenceName
     * @param start
     * @param increment
     */
    protected void writeSequence(DumpWriter dump, String sequenceName, Long start, Integer increment) throws DBCastException, IOException {
        DumpObjectHeader sHeader = new DumpObjectHeader(sequenceName, true);
        sHeader.addAttr(new TypeAttr("last", Types.LONG));
        sHeader.addAttr(new TypeAttr("increment", Types.INT));
        dump.writeObjectHeader(sHeader);
        DumpRow row = new DumpRow();
        row.addValue(DBValue.createInstance(Types.LONG, start));
        row.addValue(DBValue.createInstance(Types.INT, increment));
        dump.writeRow(row, getTmpDir());
        if (isGenerateDumpInfo()) {
            _objectInfo.add(new ObjectInfoImpl(sequenceName, 0, 1, 0, 0, 0));
        }
    }

    /**
     * Содает {@link DumpObjectHeader} по {@link ConfigObject]
     * 
     * @param cObject
     * @return
     */
    protected DumpObjectHeader createObjectTableHeader(ConfigObject cObject) {
        return new DumpObjectHeader(cObject.getRealName(), false);
    }

    /**
     * Записывает в дамп объекты по переданному хидеру
     */
    protected abstract void writeRows(DumpWriter dump, DumpObjectHeader objHeader) throws Exception;

    protected abstract boolean isDiffActionAttribute(String attrName);

    protected int countrealFiles(Set includeFileAttr) {
        return includeFileAttr == null ? 0 : includeFileAttr.size();
    }

    protected DumpRow createDumpRow(DumpObjectHeader objHeader, MObject mObject, Set includeFileAttr) throws ErrorCodeException {
        DumpRow row = new DumpRow();
        for (Iterator i = objHeader.getTypeAttrs().iterator(); i.hasNext(); ) {
            TypeAttr typeAttr = (TypeAttr) i.next();
            if (DumpHeader.DIFF_ATTR_NAME.equals(typeAttr.getAttrName())) {
                row.addValue(NULL_DIFF_DBVALUE);
                continue;
            }
            MAttribute mAttr = mObject.getAttribute(typeAttr.getAttrName());
            DBValue dbValue = DBValue.createInstance(mAttr.getType(), mAttr.getDBValue().get());
            row.addValue(dbValue);
            if (includeFileAttr != null && mAttr.isFile() && mAttr.getDBValue().get() instanceof FileValue) {
                FileValue fv = (FileValue) mAttr.getDBValue().get();
                if (fv.exists() && !compareFileValue(fv, mObject, objHeader, mAttr)) {
                    includeFileAttr.add(mAttr.getName());
                }
            }
        }
        return row;
    }

    private boolean compareFileValue(FileValue fv, MObject mObject, DumpObjectHeader objHeader, MAttribute mAttr) throws ErrorCodeException {
        return getFileComparator().compare(mObject.getId(), objHeader.getObjectName(), mAttr.getName(), fv);
    }

    protected FileComparator getFileComparator() throws ErrorCodeException {
        if (_fileComparator == null) {
            throw new ErrorCodeException(ErrorCodeException.UNKNOWN_ERROR, "File comparator is not set");
        }
        return _fileComparator;
    }

    protected void setFileComparator(FileComparator fc, Set remoteObjectNames) {
        fc.setRemoteObjectNames(remoteObjectNames);
        _fileComparator = fc;
    }

    protected SelectOptions createSelectOptions(DumpObjectHeader objHeader, DumpWriter dump) throws DBConfigException, SQLStatementException {
        Sort sort = new Sort(getDbConfig());
        sort.add(objHeader.getObjectName(), "id");
        sort.isOnlyThis(true);
        SelectAttributes selectAttrs = createSelectAttrs(objHeader);
        SelectOptions selOptions = new SelectOptions(getDbConfig().createMObject(objHeader.getObjectName(), null), null);
        selOptions.setAncestor(null);
        selOptions.setDistinct(true);
        selOptions.setGetForeignAttrs(false);
        selOptions.setLimit(null);
        selOptions.setPaths(null);
        selOptions.setSContextCollection(null);
        selOptions.setSelectAttributes(selectAttrs);
        selOptions.setSort(sort);
        return selOptions;
    }

    private SelectAttributes createSelectAttrs(DumpObjectHeader objHeader) {
        SelectAttributes selectAttrs = new SelectAttributes();
        SelectAttributesItem item = new SelectAttributesItem(objHeader.getObjectName(), objHeader.getObjectName(), true);
        for (Iterator i = objHeader.getTypeAttrs().iterator(); i.hasNext(); ) {
            TypeAttr typeAttr = (TypeAttr) i.next();
            if (!isDiffActionAttribute(typeAttr.getAttrName())) {
                item.addAttribute(typeAttr.getAttrName());
            }
        }
        selectAttrs.add(item);
        return selectAttrs;
    }

    protected void openDump(DumpWriter dump, String dumpFilePath) throws InputOutputException {
        try {
            dump.open(new BufferedOutputStream(new FileOutputStream(dumpFilePath)));
        } catch (IOException e) {
            throw new InputOutputException(e, dumpFilePath);
        }
    }

    protected abstract DumpHeader createDumpHeader(byte[] sourceConfigFile, boolean isFiles, long numberOfObjects);

    protected abstract long getNumberOfDumpedObjects();

    protected byte[] readSourceConfigFile(String configFilePath) throws InputOutputException, IOException {
        byte[] sourceConfigFile = new byte[0];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            FileInputStream fis = new FileInputStream(configFilePath);
            try {
                Stream.readTo(fis, os);
                sourceConfigFile = os.toByteArray();
            } finally {
                fis.close();
            }
        } catch (IOException e) {
            throw new InputOutputException(e, configFilePath);
        } finally {
            os.close();
        }
        return sourceConfigFile;
    }

    public File getTmpDir() {
        return _tmpDir;
    }
}
