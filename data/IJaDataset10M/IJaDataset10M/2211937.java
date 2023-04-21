package com.techstar.dmis.dto;

import java.io.Serializable;
import com.techstar.framework.service.dto.DictionaryBaseDto;

/**
 * Domain classe for 主变压器
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class ParaMainTRFDto implements Serializable {

    public ParaMainTRFDto() {
    }

    private String fcapacityratio;

    private String fwiregroup;

    private String finstallsite;

    private String ffirefightsysistmtd;

    private String fhvoutletmtd;

    private String fmvoutletmtd;

    private String flvoutletmtd;

    private int fnoloadcurrent;

    private int fnoloadloss;

    private int fhmshortci;

    private int fhlshortci;

    private int fmlshortci;

    private int fhmshortcloss;

    private int fhlshortcloss;

    private int fmlshortcloss;

    private String fweakinsulated;

    private int funtankweight;

    private int foilweight;

    private int ftotalweight;

    private String fvoltageratio;

    private String fcurrentratio;

    private int fratedcapacity;

    private String fhaveload;

    private String fcoolmethod;

    private int fselfcoolmethod;

    private String fphaseno;

    private String finsulatedielectric;

    private String fcombinationcap;

    private String ftotallyencolosed;

    private String fisthermosyphon;

    private String ftankstructure;

    private String ftankstrength;

    private int fuptankweight;

    private String fhavenoloadvolrglt;

    private String fspecdev;

    private String ffaulttestfac;

    private String ffaulttestmodel;

    private String fwaterctrlmodel;

    private String fwaterctrlfac;

    private String fishas;

    private String fhighvolcompare;

    private String fmidvolcompare;

    private String flowvolcompare;

    private String fimpedancehemo;

    private String fimpedancehems;

    private String fimpedancehome;

    private String fimpedancehsme;

    private String fnphighvolcompare;

    private String fnpmidvolcompare;

    private String fmthighvolcompare;

    private String fmtmidvolcompare;

    private java.sql.Date ffixdate;

    private java.sql.Date fexamdate;

    private java.sql.Date fassaydate;

    private String feqpid;

    private int version;

    private com.techstar.dmis.dto.EtsEquipmentDto zparamaintrf2;

    /**
     * getters and setters
     */
    public void setFcapacityratio(String fcapacityratio) {
        this.fcapacityratio = fcapacityratio;
    }

    public String getFcapacityratio() {
        return fcapacityratio;
    }

    public void setFwiregroup(String fwiregroup) {
        this.fwiregroup = fwiregroup;
    }

    public String getFwiregroup() {
        return fwiregroup;
    }

    public void setFinstallsite(String finstallsite) {
        this.finstallsite = finstallsite;
    }

    public String getFinstallsite() {
        return finstallsite;
    }

    public void setFfirefightsysistmtd(String ffirefightsysistmtd) {
        this.ffirefightsysistmtd = ffirefightsysistmtd;
    }

    public String getFfirefightsysistmtd() {
        return ffirefightsysistmtd;
    }

    public void setFhvoutletmtd(String fhvoutletmtd) {
        this.fhvoutletmtd = fhvoutletmtd;
    }

    public String getFhvoutletmtd() {
        return fhvoutletmtd;
    }

    public void setFmvoutletmtd(String fmvoutletmtd) {
        this.fmvoutletmtd = fmvoutletmtd;
    }

    public String getFmvoutletmtd() {
        return fmvoutletmtd;
    }

    public void setFlvoutletmtd(String flvoutletmtd) {
        this.flvoutletmtd = flvoutletmtd;
    }

    public String getFlvoutletmtd() {
        return flvoutletmtd;
    }

    public void setFnoloadcurrent(int fnoloadcurrent) {
        this.fnoloadcurrent = fnoloadcurrent;
    }

    public int getFnoloadcurrent() {
        return fnoloadcurrent;
    }

    public void setFnoloadloss(int fnoloadloss) {
        this.fnoloadloss = fnoloadloss;
    }

    public int getFnoloadloss() {
        return fnoloadloss;
    }

    public void setFhmshortci(int fhmshortci) {
        this.fhmshortci = fhmshortci;
    }

    public int getFhmshortci() {
        return fhmshortci;
    }

    public void setFhlshortci(int fhlshortci) {
        this.fhlshortci = fhlshortci;
    }

    public int getFhlshortci() {
        return fhlshortci;
    }

    public void setFmlshortci(int fmlshortci) {
        this.fmlshortci = fmlshortci;
    }

    public int getFmlshortci() {
        return fmlshortci;
    }

    public void setFhmshortcloss(int fhmshortcloss) {
        this.fhmshortcloss = fhmshortcloss;
    }

    public int getFhmshortcloss() {
        return fhmshortcloss;
    }

    public void setFhlshortcloss(int fhlshortcloss) {
        this.fhlshortcloss = fhlshortcloss;
    }

    public int getFhlshortcloss() {
        return fhlshortcloss;
    }

    public void setFmlshortcloss(int fmlshortcloss) {
        this.fmlshortcloss = fmlshortcloss;
    }

    public int getFmlshortcloss() {
        return fmlshortcloss;
    }

    public void setFweakinsulated(String fweakinsulated) {
        this.fweakinsulated = fweakinsulated;
    }

    public String getFweakinsulated() {
        return fweakinsulated;
    }

    public void setFuntankweight(int funtankweight) {
        this.funtankweight = funtankweight;
    }

    public int getFuntankweight() {
        return funtankweight;
    }

    public void setFoilweight(int foilweight) {
        this.foilweight = foilweight;
    }

    public int getFoilweight() {
        return foilweight;
    }

    public void setFtotalweight(int ftotalweight) {
        this.ftotalweight = ftotalweight;
    }

    public int getFtotalweight() {
        return ftotalweight;
    }

    public void setFvoltageratio(String fvoltageratio) {
        this.fvoltageratio = fvoltageratio;
    }

    public String getFvoltageratio() {
        return fvoltageratio;
    }

    public void setFcurrentratio(String fcurrentratio) {
        this.fcurrentratio = fcurrentratio;
    }

    public String getFcurrentratio() {
        return fcurrentratio;
    }

    public void setFratedcapacity(int fratedcapacity) {
        this.fratedcapacity = fratedcapacity;
    }

    public int getFratedcapacity() {
        return fratedcapacity;
    }

    public void setFhaveload(String fhaveload) {
        this.fhaveload = fhaveload;
    }

    public String getFhaveload() {
        return fhaveload;
    }

    public void setFcoolmethod(String fcoolmethod) {
        this.fcoolmethod = fcoolmethod;
    }

    public String getFcoolmethod() {
        return fcoolmethod;
    }

    public void setFselfcoolmethod(int fselfcoolmethod) {
        this.fselfcoolmethod = fselfcoolmethod;
    }

    public int getFselfcoolmethod() {
        return fselfcoolmethod;
    }

    public void setFphaseno(String fphaseno) {
        this.fphaseno = fphaseno;
    }

    public String getFphaseno() {
        return fphaseno;
    }

    public void setFinsulatedielectric(String finsulatedielectric) {
        this.finsulatedielectric = finsulatedielectric;
    }

    public String getFinsulatedielectric() {
        return finsulatedielectric;
    }

    public void setFcombinationcap(String fcombinationcap) {
        this.fcombinationcap = fcombinationcap;
    }

    public String getFcombinationcap() {
        return fcombinationcap;
    }

    public void setFtotallyencolosed(String ftotallyencolosed) {
        this.ftotallyencolosed = ftotallyencolosed;
    }

    public String getFtotallyencolosed() {
        return ftotallyencolosed;
    }

    public void setFisthermosyphon(String fisthermosyphon) {
        this.fisthermosyphon = fisthermosyphon;
    }

    public String getFisthermosyphon() {
        return fisthermosyphon;
    }

    public void setFtankstructure(String ftankstructure) {
        this.ftankstructure = ftankstructure;
    }

    public String getFtankstructure() {
        return ftankstructure;
    }

    public void setFtankstrength(String ftankstrength) {
        this.ftankstrength = ftankstrength;
    }

    public String getFtankstrength() {
        return ftankstrength;
    }

    public void setFuptankweight(int fuptankweight) {
        this.fuptankweight = fuptankweight;
    }

    public int getFuptankweight() {
        return fuptankweight;
    }

    public void setFhavenoloadvolrglt(String fhavenoloadvolrglt) {
        this.fhavenoloadvolrglt = fhavenoloadvolrglt;
    }

    public String getFhavenoloadvolrglt() {
        return fhavenoloadvolrglt;
    }

    public void setFspecdev(String fspecdev) {
        this.fspecdev = fspecdev;
    }

    public String getFspecdev() {
        return fspecdev;
    }

    public void setFfaulttestfac(String ffaulttestfac) {
        this.ffaulttestfac = ffaulttestfac;
    }

    public String getFfaulttestfac() {
        return ffaulttestfac;
    }

    public void setFfaulttestmodel(String ffaulttestmodel) {
        this.ffaulttestmodel = ffaulttestmodel;
    }

    public String getFfaulttestmodel() {
        return ffaulttestmodel;
    }

    public void setFwaterctrlmodel(String fwaterctrlmodel) {
        this.fwaterctrlmodel = fwaterctrlmodel;
    }

    public String getFwaterctrlmodel() {
        return fwaterctrlmodel;
    }

    public void setFwaterctrlfac(String fwaterctrlfac) {
        this.fwaterctrlfac = fwaterctrlfac;
    }

    public String getFwaterctrlfac() {
        return fwaterctrlfac;
    }

    public void setFishas(String fishas) {
        this.fishas = fishas;
    }

    public String getFishas() {
        return fishas;
    }

    public void setFhighvolcompare(String fhighvolcompare) {
        this.fhighvolcompare = fhighvolcompare;
    }

    public String getFhighvolcompare() {
        return fhighvolcompare;
    }

    public void setFmidvolcompare(String fmidvolcompare) {
        this.fmidvolcompare = fmidvolcompare;
    }

    public String getFmidvolcompare() {
        return fmidvolcompare;
    }

    public void setFlowvolcompare(String flowvolcompare) {
        this.flowvolcompare = flowvolcompare;
    }

    public String getFlowvolcompare() {
        return flowvolcompare;
    }

    public void setFimpedancehemo(String fimpedancehemo) {
        this.fimpedancehemo = fimpedancehemo;
    }

    public String getFimpedancehemo() {
        return fimpedancehemo;
    }

    public void setFimpedancehems(String fimpedancehems) {
        this.fimpedancehems = fimpedancehems;
    }

    public String getFimpedancehems() {
        return fimpedancehems;
    }

    public void setFimpedancehome(String fimpedancehome) {
        this.fimpedancehome = fimpedancehome;
    }

    public String getFimpedancehome() {
        return fimpedancehome;
    }

    public void setFimpedancehsme(String fimpedancehsme) {
        this.fimpedancehsme = fimpedancehsme;
    }

    public String getFimpedancehsme() {
        return fimpedancehsme;
    }

    public void setFnphighvolcompare(String fnphighvolcompare) {
        this.fnphighvolcompare = fnphighvolcompare;
    }

    public String getFnphighvolcompare() {
        return fnphighvolcompare;
    }

    public void setFnpmidvolcompare(String fnpmidvolcompare) {
        this.fnpmidvolcompare = fnpmidvolcompare;
    }

    public String getFnpmidvolcompare() {
        return fnpmidvolcompare;
    }

    public void setFmthighvolcompare(String fmthighvolcompare) {
        this.fmthighvolcompare = fmthighvolcompare;
    }

    public String getFmthighvolcompare() {
        return fmthighvolcompare;
    }

    public void setFmtmidvolcompare(String fmtmidvolcompare) {
        this.fmtmidvolcompare = fmtmidvolcompare;
    }

    public String getFmtmidvolcompare() {
        return fmtmidvolcompare;
    }

    public void setFfixdate(java.sql.Date ffixdate) {
        this.ffixdate = ffixdate;
    }

    public java.sql.Date getFfixdate() {
        return ffixdate;
    }

    public void setFexamdate(java.sql.Date fexamdate) {
        this.fexamdate = fexamdate;
    }

    public java.sql.Date getFexamdate() {
        return fexamdate;
    }

    public void setFassaydate(java.sql.Date fassaydate) {
        this.fassaydate = fassaydate;
    }

    public java.sql.Date getFassaydate() {
        return fassaydate;
    }

    public void setFeqpid(String feqpid) {
        this.feqpid = feqpid;
    }

    public String getFeqpid() {
        return feqpid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setZparamaintrf2(com.techstar.dmis.dto.EtsEquipmentDto zparamaintrf2) {
        this.zparamaintrf2 = zparamaintrf2;
    }

    public com.techstar.dmis.dto.EtsEquipmentDto getZparamaintrf2() {
        return zparamaintrf2;
    }
}
