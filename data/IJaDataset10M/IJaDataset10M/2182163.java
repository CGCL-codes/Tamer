package com.intrigueit.myc2i.member.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.intrigueit.myc2i.common.ServiceConstants;
import com.intrigueit.myc2i.common.view.BasePage;
import com.intrigueit.myc2i.common.view.CommonValidator;
import com.intrigueit.myc2i.common.view.ViewDataProvider;
import com.intrigueit.myc2i.member.domain.Member;
import com.intrigueit.myc2i.member.service.MemberService;

@Component("myProfileViewHandler")
@Scope("session")
public class MyProfileViewHandler extends BasePage implements Serializable {

    /**
   * Generated serial version ID
   */
    private static final long serialVersionUID = 7503050945222001002L;

    /** Initialize the Logger */
    protected static final Logger logger = Logger.getLogger(MyProfileViewHandler.class);

    private MemberService memberService;

    private Member currentMember;

    private ViewDataProvider viewDataProvider;

    private CommonValidator commonValidator;

    /** Available transfer methods */
    private ArrayList<SelectItem> martialStatusList;

    private ArrayList<SelectItem> birthYearlist;

    private ArrayList<SelectItem> knowledgeLevelList;

    private String confirmPass;

    private Boolean agree = false;

    private ArrayList<SelectItem> question1List;

    private ArrayList<SelectItem> question2List;

    private Hashtable<String, String> memberTypeHash;

    private String userType;

    private String selTabName;

    /**
   * @return the selTabName
   */
    public String getSelTabName() {
        return selTabName;
    }

    /**
   * @param selTabName the selTabName to set
   */
    public void setSelTabName(String selTabName) {
        this.selTabName = selTabName;
    }

    @Autowired
    public MyProfileViewHandler(MemberService memberService, ViewDataProvider viewDataProvider) {
        this.memberService = memberService;
        this.viewDataProvider = viewDataProvider;
        commonValidator = new CommonValidator();
        this.initialize();
    }

    public void initialize() {
        setSecHeaderMsg("");
        loadMember();
    }

    public void loadMember() {
        try {
            logger.debug(" Load Member ");
            Long recordId = this.getMember().getMemberId();
            this.currentMember = memberService.findById(recordId);
            if (this.currentMember.getTypeId() != null) {
                setUserType(parseMemberType("" + this.currentMember.getTypeId()));
            }
            this.setActionType(ServiceConstants.UPDATE);
        } catch (Exception ex) {
            logger.error("Unable to load Members:" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public boolean validate() {
        logger.debug(" Validating member ");
        StringBuffer errorMessage = new StringBuffer();
        StringBuffer tabName = new StringBuffer();
        tabName.append("P");
        boolean flag = commonValidator.validateMember(this.currentMember, getUserType(), ServiceConstants.UPDATE, confirmPass, errorMessage, tabName);
        if (!flag) setErrorMessage(this.getText("common_error_header") + errorMessage.toString());
        this.setSelTabName(tabName.toString());
        return flag;
    }

    private boolean validationPhase2() {
        logger.debug(" Validating member ");
        boolean flag = true;
        StringBuffer errorMessage = new StringBuffer();
        if (this.getAgree() == false) {
            if (!flag) errorMessage.append("<br />");
            errorMessage.append(this.getText("common_error_prefix")).append(" ").append(this.getText("member_validation_licence_agree"));
            flag = false;
        }
        if (!flag) setErrorMessage(this.getText("common_error_header") + errorMessage.toString());
        return flag;
    }

    public void setCommonData(String action) {
        setSecHeaderMsg("");
        try {
            Date dt = new Date();
            this.currentMember.setRecordUpdaterId("" + this.getMember().getMemberId());
            this.currentMember.setLastUpdated(dt);
        } catch (Exception e) {
            setSecHeaderMsg(this.getText("invalid_seesion_message"));
            logger.error(" Unable to set common data :" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void reloadUser() {
        Long recordId = this.getMember().getMemberId();
        this.currentMember = memberService.findById(recordId);
    }

    public void updateUser() {
        logger.debug(" Updating user ");
        setErrorMessage("");
        try {
            String mTypeId = "";
            if (validate()) {
                if (this.currentMember.getCountry() != null && this.currentMember.getCountry().equals("-1")) {
                    this.currentMember.setCountry(null);
                }
                this.memberService.update(this.currentMember);
                logger.debug("Member updated: " + this.currentMember.getMemberId());
                this.setErrorMessage(this.getText("update_success_message"));
                this.setMsgType(ServiceConstants.INFO);
            }
        } catch (Exception e) {
            if (this.currentMember.getMemberId() != null) {
                this.currentMember.setMemberId(null);
            }
            setErrorMessage(this.getText("common_system_error"));
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
   * @return the memberTypeHash
   */
    public Hashtable<String, String> getMemberTypeHash() {
        if (memberTypeHash == null) {
            this.memberTypeHash = viewDataProvider.getMemberTypeHash();
        }
        return memberTypeHash;
    }

    @SuppressWarnings("unchecked")
    private String parseMemberType(String type) {
        Hashtable<String, String> mType = this.getMemberTypeHash();
        Set keySet = mType.keySet();
        Iterator it = keySet.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (type.equals(mType.get(key))) {
                return key;
            }
        }
        return "";
    }

    /**
   * @return the userType
   */
    public String getUserType() {
        return userType;
    }

    /**
   * @param userType
   *          the userType to set
   */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
   * @return the currentMember
   */
    public Member getCurrentMember() {
        if (currentMember == null) {
            System.out.println(" Init member ");
            currentMember = new Member();
        }
        return currentMember;
    }

    /**
   * @param currentMember
   *          the currentMember to set
   */
    public void setCurrentMember(Member currentMember) {
        this.currentMember = currentMember;
    }

    public List<SelectItem> getStatesList() {
        return viewDataProvider.getStateList();
    }

    public List<SelectItem> getCountryList() {
        return this.viewDataProvider.getCountryList();
    }

    public List<SelectItem> getEthinicityList() {
        return this.viewDataProvider.getEthinicityList();
    }

    public ArrayList<SelectItem> getMartialStatusList() {
        if (martialStatusList == null) {
            this.martialStatusList = this.viewDataProvider.getMaritialStatusList();
        }
        return martialStatusList;
    }

    public void setMartialStatusList(ArrayList<SelectItem> martialStatusList) {
        this.martialStatusList = martialStatusList;
    }

    public ArrayList<SelectItem> getBirthYearlist() {
        if (birthYearlist == null) {
            this.birthYearlist = ViewDataProvider.getYearList();
        }
        return birthYearlist;
    }

    public void setBirthYearlist(ArrayList<SelectItem> birthYearlist) {
        this.birthYearlist = birthYearlist;
    }

    public List<SelectItem> getProfessionList() {
        return viewDataProvider.getProfessionList();
    }

    public List<SelectItem> getMadhabList() {
        return this.viewDataProvider.getMadhabList();
    }

    public ArrayList<SelectItem> getKnowledgeLevelList() {
        if (knowledgeLevelList == null) {
            this.knowledgeLevelList = ViewDataProvider.getKnowledgeLevelList();
        }
        return knowledgeLevelList;
    }

    public void setKnowledgeLevelList(ArrayList<SelectItem> knowledgeLevelList) {
        this.knowledgeLevelList = knowledgeLevelList;
    }

    public List<SelectItem> getReligionList() {
        return this.viewDataProvider.getReligionList();
    }

    public Boolean getAgree() {
        return agree;
    }

    public void setAgree(Boolean agree) {
        this.agree = agree;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }

    public ArrayList<SelectItem> getQuestion1List() {
        if (question1List == null) {
            this.question1List = viewDataProvider.getQuestionList();
        }
        return question1List;
    }

    public void setQuestion1List(ArrayList<SelectItem> question1List) {
        this.question1List = question1List;
    }

    public ArrayList<SelectItem> getQuestion2List() {
        if (question2List == null) {
            this.question2List = viewDataProvider.getQuestionList();
        }
        return question2List;
    }

    public void setQuestion2List(ArrayList<SelectItem> question2List) {
        this.question2List = question2List;
    }
}
