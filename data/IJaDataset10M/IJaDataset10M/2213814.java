package it.yahoo.carlo.politi.finder.bean;

import java.util.ArrayList;
import java.util.Vector;

/**
 * JavaBean preposto alla memorizzazione dei parametri utili all'attivazione delle varie funzioni di Finder
 *
 * @author Carlo Politi
 */
public class FinderParamsBean {

    private String dbDir = "";
    private String fileConfigCfg = "";
    private String fileConfigMaF = "";
    private String listName = "";
    private String logName = "";
    private String moveHereDir = "";
    private Vector<FinderArgsBean> listaParams = new Vector<>();
    private Vector<String> listaDir = new Vector<>();
    private boolean funzCfg = false;
    private boolean funzClearCache = false;
    private boolean funzClearName = false;
    private boolean funzConfirm = false;
    private boolean funzCorrectExt = true;
    private boolean funzCt = false;
    private boolean funzDelSkipPresent = false;
    private boolean funzDelete = false;
    private boolean funzDeleteMD = false;
    private boolean funzEditConfig = false;
    private boolean funzFindFile = false;
    private boolean funzFp = false;
    private boolean funzHelp = false;
    private boolean funzIgnoreCase = false;
    private boolean funzIterPeriod = false;
    private boolean funzIterTime = false;
    private boolean funzList = false;
    private boolean funzLog = false;
    private boolean funzMD = false;
    private boolean funzMDDB = false;
    private boolean funzMDx2 = false;
    private boolean funzMaF = false;
    private boolean funzMove = false;
    private boolean funzMoveHere = false;
    private boolean funzNoDownload = false;
    private boolean funzNoGUI = false;
    private boolean funzNoSplash = false;
    private boolean funzOverWrite = false;
    private boolean funzPreserveDate = false;
    private boolean funzQuiet = false;
    private boolean funzRecursive = false;
    private boolean funzRemoveDir = false;
    private boolean funzReverseOrder = false;
    private boolean funzSearchDeleteFile = false;
    private boolean funzShowDB = false;
    private boolean funzShowEquals = false;
    private boolean funzShowFileConfig = false;
    private boolean funzShowVersion = false;
    private boolean funzSkipPresent = false;
    private boolean funzTrim = false;
    private boolean funzUseOnlyFP = false;
    private boolean funzVerbose = false;
    private int iterationPeriod = 300000; // 5 min
    private int iterationTime = 1;
    private int modalita = -1;
    private int trimLen = -1;
    private ArrayList etichetteDrive = new ArrayList();

    public FinderParamsBean() {
    }

    public boolean isFunzClearCache() {
        return funzClearCache;
    }

    public void setFunzClearCache(boolean funzClearCache) {
        this.funzClearCache = funzClearCache;
    }

    public boolean isFunzCt() {
        return funzCt;
    }

    public void setFunzCt(boolean funzCt) {
        this.funzCt = funzCt;
    }

    public boolean isFunzDelete() {
        return funzDelete;
    }

    public void setFunzDelete(boolean funzDelete) {
        this.funzDelete = funzDelete;
    }

    public boolean isFunzDeleteMD() {
        return funzDeleteMD;
    }

    public void setFunzDeleteMD(boolean funzDeleteMD) {
        this.funzDeleteMD = funzDeleteMD;
    }

    public boolean isFunzFindFile() {
        return funzFindFile;
    }

    public void setFunzFindFile(boolean funzFindFile) {
        this.funzFindFile = funzFindFile;
    }

    public boolean isFunzFp() {
        return funzFp;
    }

    public void setFunzFp(boolean funzFp) {
        this.funzFp = funzFp;
    }

    public boolean isFunzHelp() {
        return funzHelp;
    }

    public void setFunzHelp(boolean funzHelp) {
        this.funzHelp = funzHelp;
    }

    public boolean isFunzIgnoreCase() {
        return funzIgnoreCase;
    }

    public void setFunzIgnoreCase(boolean funzIgnoreCase) {
        this.funzIgnoreCase = funzIgnoreCase;
    }

    public boolean isFunzList() {
        return funzList;
    }

    public void setFunzList(boolean funzList) {
        this.funzList = funzList;
    }

    public boolean isFunzLog() {
        return funzLog;
    }

    public void setFunzLog(boolean funzLog) {
        this.funzLog = funzLog;
    }

    public boolean isFunzMD() {
        return funzMD;
    }

    public void setFunzMD(boolean funzMD) {
        this.funzMD = funzMD;
    }

    public boolean isFunzMDx2() {
        return funzMDx2;
    }

    public void setFunzMDx2(boolean funzMDx2) {
        this.funzMDx2 = funzMDx2;
    }

    public boolean isFunzMove() {
        return funzMove;
    }

    public void setFunzMove(boolean funzMove) {
        this.funzMove = funzMove;
    }

    public boolean isFunzMoveHere() {
        return funzMoveHere;
    }

    public void setFunzMoveHere(boolean funzMoveHere) {
        this.funzMoveHere = funzMoveHere;
    }

    public boolean isFunzQuiet() {
        return funzQuiet;
    }

    public void setFunzQuiet(boolean funzQuiet) {
        this.funzQuiet = funzQuiet;
    }

    public boolean isFunzRecursive() {
        return funzRecursive;
    }

    public void setFunzRecursive(boolean funzRecursive) {
        this.funzRecursive = funzRecursive;
    }

    public boolean isFunzRemoveDir() {
        return funzRemoveDir;
    }

    public void setFunzRemoveDir(boolean funzRemoveDir) {
        this.funzRemoveDir = funzRemoveDir;
    }

    public boolean isFunzSearchDeleteFile() {
        return funzSearchDeleteFile;
    }

    public void setFunzSearchDeleteFile(boolean funzSearchDeleteFile) {
        this.funzSearchDeleteFile = funzSearchDeleteFile;
    }

    public boolean isFunzShowEquals() {
        return funzShowEquals;
    }

    public void setFunzShowEquals(boolean funzShowEquals) {
        this.funzShowEquals = funzShowEquals;
    }

    public boolean isFunzShowFileConfig() {
        return funzShowFileConfig;
    }

    public void setFunzShowFileConfig(boolean funzShowFileConfig) {
        this.funzShowFileConfig = funzShowFileConfig;
    }

    public boolean isFunzVerbose() {
        return funzVerbose;
    }

    public void setFunzVerbose(boolean funzVerbose) {
        this.funzVerbose = funzVerbose;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getMoveHereDir() {
        return moveHereDir;
    }

    public void setMoveHereDir(String moveHereDir) {
        this.moveHereDir = moveHereDir;
    }

    public Vector<FinderArgsBean> getListaParams() {
        return listaParams;
    }

    public void setListaParams(Vector<FinderArgsBean> listaParams) {
        this.listaParams = listaParams;
    }

    public Vector<String> getListaDir() {
        return listaDir;
    }

    public void setListaDir(Vector<String> listaDir) {
        this.listaDir = listaDir;
    }

    public boolean isFunzNoGUI() {
        return funzNoGUI;
    }

    public void setFunzNoGUI(boolean funzNoGUI) {
        this.funzNoGUI = funzNoGUI;
    }

    public String getFileConfigMaF() {
        return fileConfigMaF;
    }

    public void setFileConfigMaF(String fileConfigMa) {
        this.fileConfigMaF = fileConfigMa;
    }

    public boolean isFunzMaF() {
        return funzMaF;
    }

    public void setFunzMaF(boolean funzMaF) {
        this.funzMaF = funzMaF;
    }

    public boolean isFunzEditConfig() {
        return funzEditConfig;
    }

    public void setFunzEditConfig(boolean funzEditConfig) {
        this.funzEditConfig = funzEditConfig;
    }

    public boolean isFunzOverWrite() {
        return funzOverWrite;
    }

    public void setFunzOverWrite(boolean funzOverWrite) {
        this.funzOverWrite = funzOverWrite;
    }

    public boolean isFunzSkipPresent() {
        return funzSkipPresent;
    }

    public void setFunzSkipPresent(boolean funzSkipPresent) {
        this.funzSkipPresent = funzSkipPresent;
    }

    public boolean isFunzDelSkipPresent() {
        return funzDelSkipPresent;
    }

    public void setFunzDelSkipPresent(boolean funzDelSkipPresent) {
        this.funzDelSkipPresent = funzDelSkipPresent;
    }

    public boolean isFunzConfirm() {
        return funzConfirm;
    }

    public void setFunzConfirm(boolean funzConfirm) {
        this.funzConfirm = funzConfirm;
    }

    public boolean isFunzMDDB() {
        return funzMDDB;
    }

    public void setFunzMDDB(boolean funzMDDB) {
        this.funzMDDB = funzMDDB;
    }

    public String getDbDir() {
        return dbDir;
    }

    public void setDbDir(String dbDir) {
        this.dbDir = dbDir;
    }

    public boolean isFunzShowDB() {
        return funzShowDB;
    }

    public void setFunzShowDB(boolean funzShowDB) {
        this.funzShowDB = funzShowDB;
    }

    public boolean isFunzNoSplash() {
        return funzNoSplash;
    }

    public void setFunzNoSplash(boolean funzNoSplash) {
        this.funzNoSplash = funzNoSplash;
    }

    public int getModalita() {
        return modalita;
    }

    public void setModalita(int modalita) {
        this.modalita = modalita;
    }

    public boolean isFunzCfg() {
        return funzCfg;
    }

    public void setFunzCfg(boolean funzCfg) {
        this.funzCfg = funzCfg;
    }

    public String getFileConfigCfg() {
        return fileConfigCfg;
    }

    public void setFileConfigCfg(String fileConfigCfg) {
        this.fileConfigCfg = fileConfigCfg;
    }

    public int getIterationPeriod() {
        return iterationPeriod;
    }

    public void setIterationPeriod(int iterationPeriod) {
        this.iterationPeriod = iterationPeriod;
    }

    public boolean isFunzIterPeriod() {
        return funzIterPeriod;
    }

    public void setFunzIterPeriod(boolean funzIterPeriod) {
        this.funzIterPeriod = funzIterPeriod;
    }

    public boolean isFunzIterTime() {
        return funzIterTime;
    }

    public void setFunzIterTime(boolean funzIterTime) {
        this.funzIterTime = funzIterTime;
    }

    public int getIterationTime() {
        return iterationTime;
    }

    public void setIterationTime(int iterationTime) {
        this.iterationTime = iterationTime;
    }

    public boolean isFunzShowVersion() {
        return funzShowVersion;
    }

    public void setFunzShowVersion(boolean funzShowVersion) {
        this.funzShowVersion = funzShowVersion;
    }

    public boolean isFunzTrim() {
        return funzTrim;
    }

    public void setFunzTrim(boolean funzTrim) {
        this.funzTrim = funzTrim;
    }

    public int getTrimLen() {
        return trimLen;
    }

    public void setTrimLen(int trimLen) {
        this.trimLen = trimLen;
    }

    public boolean isFunzClearName() {
        return funzClearName;
    }

    public void setFunzClearName(boolean funzClearName) {
        this.funzClearName = funzClearName;
    }

    public boolean isFunzCorrectExt() {
        return funzCorrectExt;
    }

    public void setFunzCorrectExt(boolean funzCorrectExt) {
        this.funzCorrectExt = funzCorrectExt;
    }

    public boolean isFunzReverseOrder() {
        return funzReverseOrder;
    }

    public void setFunzReverseOrder(boolean funzReverseOrder) {
        this.funzReverseOrder = funzReverseOrder;
    }

    public boolean isFunzUseOnlyFP() {
        return funzUseOnlyFP;
    }

    public void setFunzUseOnlyFP(boolean funzUseOnlyFP) {
        this.funzUseOnlyFP = funzUseOnlyFP;
    }

    public boolean isFunzPreserveDate() {
        return funzPreserveDate;
    }

    public void setFunzPreserveDate(boolean funzPreserveDate) {
        this.funzPreserveDate = funzPreserveDate;
    }

    public boolean isFunzNoDownload() {
        return funzNoDownload;
    }

    public void setFunzNoDownload(boolean funzNoDownload) {
        this.funzNoDownload = funzNoDownload;
    }

    public ArrayList getEtichetteDrive() {
        return etichetteDrive;
    }

    public void setEtichetteDrive(ArrayList etichetteDrive) {
        this.etichetteDrive = etichetteDrive;
    }

}
