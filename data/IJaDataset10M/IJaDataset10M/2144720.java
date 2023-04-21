package jlokg.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jlokg.account.LKGVentilAccountLine;

/**
 * 
 * 
 * 
 * @hibernate.class table = "finfileline"
 */
public class LKGFinancialFileLine {

    public static final String kTypeAccountCredit = "+";

    public static final String kTypeAccounDebit = "-";

    private String Account;

    /**
  *   @hibernate.property
  */
    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
    }

    private boolean Augmentable;

    /**
  *   @hibernate.property
  */
    public boolean getAugmentable() {
        return Augmentable;
    }

    public void setAugmentable(boolean Augmentable) {
        this.Augmentable = Augmentable;
    }

    private String Comment;

    /**
  *   @hibernate.property
  */
    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    private boolean Conserve;

    /**
  *   @hibernate.property
  */
    public boolean getConserve() {
        return Conserve;
    }

    public void setConserve(boolean Conserve) {
        this.Conserve = Conserve;
    }

    private String MasterFinFile;

    /**
  *   @hibernate.property
  */
    public String getMasterFinFile() {
        return MasterFinFile;
    }

    public void setMasterFinFile(String MasterFinFile) {
        this.MasterFinFile = MasterFinFile;
    }

    private double Honoraire;

    /**
  *   @hibernate.property
  */
    public double getHonoraire() {
        return Honoraire;
    }

    public void setHonoraire(double Honoraire) {
        this.Honoraire = Honoraire;
    }

    private double Montant;

    /**
  *   @hibernate.property
  */
    public double getMontant() {
        return Montant;
    }

    public void setMontant(double Montant) {
        this.Montant = Montant;
    }

    private int Number;

    /**
  *   @hibernate.property
  */
    public int getNumber() {
        return Number;
    }

    public void setNumber(int Number) {
        this.Number = Number;
    }

    private boolean Systematique;

    /**
  *   @hibernate.property
  */
    public boolean getSystematique() {
        return Systematique;
    }

    public void setSystematique(boolean Systematique) {
        this.Systematique = Systematique;
    }

    private String Title;

    /**
  *   @hibernate.property
  */
    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    private String Type;

    /**
  *   @hibernate.property
  */
    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    private String LastUser;

    /**
  *   @hibernate.property
  */
    public String getLastUser() {
        return LastUser;
    }

    public void setLastUser(String LastUser) {
        this.LastUser = LastUser;
    }

    private java.util.Date DateModification;

    /**
  *   @hibernate.property
  */
    public java.util.Date getDateModification() {
        return DateModification;
    }

    public void setDateModification(java.util.Date DateModification) {
        this.DateModification = DateModification;
    }

    private java.util.Date DateCreation;

    /**
  *   @hibernate.property
  */
    public java.util.Date getDateCreation() {
        return DateCreation;
    }

    public void setDateCreation(java.util.Date DateCreation) {
        this.DateCreation = DateCreation;
    }

    private String Code;

    /**
  *   @hibernate.id
  *     generator-class="assigned"
  */
    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public LKGFinancialFileLine() {
        Account = "";
        Code = "";
        Comment = "";
        LastUser = "";
        MasterFinFile = "";
        Title = "";
        Type = "";
    }

    public LKGAccountLine CreateAccountLine(String pOperationCode, String pSign) {
        LKGAccountLine result = new LKGAccountLine();
        result.setOperation(pOperationCode);
        result.setDateFog(new Date());
        result.setCompte(this.Account);
        if (pSign.equals(LKGFinancialFileLine.kTypeAccounDebit)) {
            result.setDebit(this.Montant);
        } else if (pSign.equals(LKGFinancialFileLine.kTypeAccountCredit)) {
            result.setCredit(this.Montant);
        }
        result.setTitre(this.Title);
        return result;
    }

    public LKGVentilAccountLine CreateVentilAccountLine(String pOperationCode, String pFogCode, String pSign) {
        LKGVentilAccountLine result = new LKGVentilAccountLine();
        result.setOperation(pOperationCode);
        result.setDateAcc(new Date());
        result.setCompte(this.Account);
        result.setIntitule(this.Title);
        if (pSign.equals(LKGFinancialFileLine.kTypeAccounDebit)) {
            result.setDebit(this.Montant);
        } else if (pSign.equals(LKGFinancialFileLine.kTypeAccountCredit)) {
            result.setCredit(this.Montant);
        }
        result.setFog(pFogCode);
        result.setTitre(this.Title);
        result.setHonoraire(this.Honoraire);
        return result;
    }

    public List CreateDCVentilAccountLines(String pOperationCode, String pFogCode, String pSign) {
        ArrayList result = new ArrayList();
        LKGVentilAccountLine localVentilLine;
        double localHonoraire = (this.Montant * this.Honoraire) / 100;
        localVentilLine = new LKGVentilAccountLine();
        localVentilLine.setOperation(pOperationCode);
        localVentilLine.setDateAcc(new Date());
        localVentilLine.setCompte(this.Account);
        localVentilLine.setIntitule(this.Title);
        if (pSign.equals(LKGFinancialFileLine.kTypeAccounDebit)) {
            localVentilLine.setDebit(this.Montant - localHonoraire);
        } else if (pSign.equals(LKGFinancialFileLine.kTypeAccountCredit)) {
            localVentilLine.setCredit(this.Montant - localHonoraire);
        }
        localVentilLine.setFog(pFogCode);
        localVentilLine.setTitre(this.Title);
        result.add(localVentilLine);
        localVentilLine = new LKGVentilAccountLine();
        localVentilLine.setOperation(pOperationCode);
        localVentilLine.setCompte(jlokg.engine.JLoKGEngine.getJLoKGEngine().getSimpleParm(jlokg.engine.JLoKGEngine.knhParmHonaireCompte));
        localVentilLine.setCompte(this.Account);
        localVentilLine.setIntitule(jlokg.engine.JLoKGEngine.knhLKGLabelHonoraires);
        localVentilLine.setTitre(jlokg.engine.JLoKGEngine.knhLKGLabelHonoraires + " " + this.Title);
        if (pSign.equals(LKGFinancialFileLine.kTypeAccounDebit)) {
            localVentilLine.setDebit(localHonoraire);
        } else if (pSign.equals(LKGFinancialFileLine.kTypeAccountCredit)) {
            localVentilLine.setCredit(localHonoraire);
        }
        localVentilLine.setFog(pFogCode);
        localVentilLine.setTitre(this.Title);
        result.add(localVentilLine);
        return result;
    }

    public LKGFinancialFileLine NewFinancialLine() {
        LKGFinancialFileLine result = new LKGFinancialFileLine();
        result.setAccount(this.Account);
        result.setAugmentable(this.Augmentable);
        result.setConserve(this.Conserve);
        result.setHonoraire(this.Honoraire);
        result.setMontant(this.Montant);
        result.setNumber(this.Number);
        result.setSystematique(this.Systematique);
        result.setTitle(this.Title);
        result.setType(this.Type);
        return result;
    }
}
