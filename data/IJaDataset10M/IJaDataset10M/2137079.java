package com.student.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.student.service.DormManagerService;
import com.student.service.FillDataService;
import com.student.util.PageInfo;
import com.student.vo.ClassInfoVo;
import com.student.vo.DormRpAssociationVo;
import com.student.vo.DormVo;
import com.student.vo.StudentVo;

/**
 * �������Action��
 * DormManagerAction
 * 2011-10-28 ����07:46:35
 * ��ΰ�� 
 * @version 1.0.0
 *
 */
@Component("DormManagerAction")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RemoteProxy(name = "dormManagerAction")
public class DormManagerAction extends SuperAction {

    private FillDataService fillDataService;

    private DormManagerService service;

    private Map<Long, String> professionMap;

    private Map<Long, String> gradeMap;

    public Map<Long, String> getGradeMap() {
        return gradeMap;
    }

    public void setGradeMap(Map<Long, String> gradeMap) {
        this.gradeMap = gradeMap;
    }

    public Map<Long, String> getProfessionMap() {
        return professionMap;
    }

    public void setProfessionMap(Map<Long, String> professionMap) {
        this.professionMap = professionMap;
    }

    public FillDataService getFillDataService() {
        return fillDataService;
    }

    @Resource
    public void setFillDataService(FillDataService fillDataService) {
        this.fillDataService = fillDataService;
    }

    private List<String> buildNumbers;

    public List<String> getBuildNumbers() {
        return buildNumbers;
    }

    public void setBuildNumbers(List<String> buildNumbers) {
        this.buildNumbers = buildNumbers;
    }

    @Resource
    public void setService(DormManagerService service) {
        this.service = service;
    }

    /**
	 * 
	 * queryDormAction ��ʼ�������ѯҳ���������

	 *@return String
	 * @since  1.0
	 */
    public String queryDormAction() {
        try {
            buildNumbers = this.service.getAllBuildNumber();
            this.logSuccessString("queryDormAction", "��ʼ�������ѯҳ���������", "�ɹ���ʼ��רҵ��Ϣ");
        } catch (Exception e) {
            this.logFailureString("queryDormAction", "��ʼ�������ѯҳ���������", e);
            e.printStackTrace();
        }
        return SUCCESS;
    }

    /**
	 * 
	 * queryDormOfClass�鿴�༶����

	 *@return String
	 * @exception 
	 * ��ΰ��
	 */
    public String queryDormOfClass() {
        try {
            professionMap = this.fillDataService.getAllProfessionName(this.getCollege().getCollegeId());
            gradeMap = this.fillDataService.getAllGradeName();
            this.logSuccessString("queryDormOfClass", "�鿴�༶����", "����ѧԺidΪ" + this.getCollege().getCollegeId() + "��רҵ");
        } catch (Exception e) {
            this.logFailureString("queryDormOfClass", "�鿴�༶����", e);
            e.printStackTrace();
        }
        return SUCCESS;
    }

    /**
	 * 
	 * gainClassList���ҳ�洫�����Ĳ���ȡ��ָ��רҵ�İ༶�б�
	 * @param professionIdרҵid  gradeId�꼶�б�

	 *@return Map<Long,String>Map��Ű༶�б?���ڵ�keyΪ�༶id,value Ϊ�༶��
	 * @exception 
	 * ��ΰ��
	 */
    public Map<Long, String> gainClassList(Long professionId, Long gradeId) {
        Map<Long, String> classInfoMap;
        try {
            classInfoMap = this.fillDataService.getAllClassName(professionId, gradeId);
            this.logSuccessString("gainClassList", "���ҳ�洫�����Ĳ���ȡ��ָ��רҵ�İ༶�б�", "����רҵidΪ" + professionId + "�꼶idΪ" + gradeId + "�İ༶");
            return classInfoMap;
        } catch (Exception e) {
            this.logFailureString("gainClassList", "���ҳ�洫�����Ĳ���ȡ��ָ��רҵ�İ༶�б�", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 
	 * gainFloorNumber��ݴ������Ķ���ȡ���N�������Բ���
	 * @param buildNumber ����
	 * @return 
	 *@return List<String>�����б�
	 * @exception 
	 * @since  1.0
	 * ��ΰ��
	 */
    @RemoteMethod
    public List<String> gainFloorNumber(String buildNumber) {
        try {
            this.logSuccessString("gainFloorNumber", "��ʼ�������ѯҳ���������", "�ɹ���ʼ��רҵ��Ϣ");
            return service.getFloorNumberByBuildNumber(buildNumber);
        } catch (Exception e) {
            this.logFailureString("gainFloorNumber", "��ʼ�������ѯҳ���������", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * �����������getDormList����ȡ��������Ϣ
	 * @author ������ģ�2011-10-31
	 * @param sex �Ա�
	 * @param buildNumber ����
	 * @param floorNumber ����
	 * @param condition �մ�λ�������
	 * @param surplusBed �մ�λ��
	 * @param dormNumber �����
	 * @param dormType �������
	 * @return �����б� nullʱ�����Ҳ�������
	 * @throws Exception
	 * 
	 */
    @RemoteMethod
    public Map<String, Object> getDormList(Boolean sex, String buildNumber, String floorNumber, String condition, String surplusBed, String dormNumber, String dormType, PageInfo pageInfo) {
        try {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            List<DormVo> dv = this.service.getDormList(this.getCollege().getCollegeId(), sex, buildNumber, floorNumber, condition, surplusBed, dormNumber, dormType, pageInfo);
            resultMap.put("dormVoList", dv);
            resultMap.put("pageInfo", pageInfo);
            this.logSuccessString("getDormList", "���ɸѡ������ѯ������Ϣ", "�ɹ���ѯ������Ϣ");
            return resultMap;
        } catch (Exception e) {
            this.logFailureString("getDormList", "���ɸѡ������ѯ������Ϣ", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * ����deleteDorm����ɾ������
	 * @author ������     2011-11-1
	 * @param dormId ����Id
	 * @return Bollean �Ƿ������ɾ��
	 */
    @RemoteMethod
    public Boolean deleteDorm(Long dormId) {
        try {
            Boolean bl = this.service.deleteDorm(dormId);
            this.logSuccessString("deleteDorm", "ɾ������", "�ɹ�ɾ������");
            return bl;
        } catch (Exception e) {
            this.logFailureString("deleteDorm", "ɾ������", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * @author ������ 2011-11-1
	 * setForbidden �޸�����״̬
	 * @param dormForbidden ����״̬
	 * @param dormId  ����Id
	 * @throws Exception
	 * @return 
	 */
    @RemoteMethod
    public DormVo setForbidden(Boolean dormForbidden, Long dormId) {
        try {
            DormVo dormVo = this.service.setForbidden(dormForbidden, dormId);
            this.logSuccessString("setForbidden", "�޸�����״̬", "������idΪ" + dormId + "�������״̬��" + (dormForbidden ? "����" : "����"));
            return dormVo;
        } catch (Exception e) {
            this.logFailureString("setForbidden", "�޸�����״̬", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 
	 * loadClassDormDetail(������һ�仰�����������������)
	 * @param professionId//רҵid
	 * @param classId//�༶id
	 * @return 
	 *@return List<ClassInfoVo>
	 * @exception 
	 * ��ΰ��
	 */
    @RemoteMethod
    public ClassInfoVo loadClassDormDetail(String className) {
        try {
            ClassInfoVo classInfoVo = this.service.getDormOfClass(className);
            this.logSuccessString("loadClassDormDetail", "�༶" + className + "�İ༶����", "�ɹ�");
            return classInfoVo;
        } catch (Exception e) {
            this.logFailureString("loadClassDormDetail", "�༶" + className + "�İ༶����", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * @author ������ 2011-11-1
	 * updateDorm ��������
	 * @param updataDorm У��
	 * @param college  ѧԺ
	 * @param profession רҵ
	 * @param classInfo �༶
	 * @param sex �Ա�
	 * @param dormBedNumber ��λ��
	 * @throws Exception
	 * @return Dorm����
	 */
    @RemoteMethod
    public DormVo updataDorm(Long dormId, Long collegeId, Long professionId, Long classId, Boolean sex, Integer dormBedNumber) {
        try {
            DormVo dormVo = new DormVo();
            dormVo.setDormId(dormId);
            dormVo.setCollegeId(collegeId);
            dormVo.setProfessionId(professionId);
            dormVo.setClassId(classId);
            dormVo.setSex(sex);
            dormVo.setDormBedNumber(dormBedNumber);
            this.logSuccessString("updataDorm", "����������Ϣ", "�ɹ�����������Ϣ");
            DormVo dormtempDorm = service.updataDorm(dormVo);
            return dormtempDorm;
        } catch (Exception e) {
            this.logFailureString("updataDorm", "����������Ϣ", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
		 * 
		 * showStudentInfo���ѧ��ID��ȡѧ�����Ϣ
		 * @author ����ǿ
		 * @param studentIdѧ��ID
		 * @return StudentVo����
		 * @throws Exception
		 *@return List<ClassInfoVo>
		 * @exception
		 * @since 1.0
		 */
    @RemoteMethod
    public StudentVo showStudentInfo(Long studentId) throws Exception {
        try {
            StudentVo studentVo = this.service.showStudentInfo(studentId);
            this.logSuccessString("showStudentInfo", "��ȡѧ�����Ϣ", "�ɹ���ȡѧ�����Ϣ");
            return studentVo;
        } catch (Exception e) {
            this.logFailureString("showStudentInfo", "��ȡѧ�����Ϣ", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
		 * @author ������ 2011-11-7
		 * setForbidden ��ѯ����Υ�����
		 * @param dormId ����Id
		 * @throws Exception
		 * @return
		 */
    @RemoteMethod
    public List<DormRpAssociationVo> getDormViolateBydormId(Long dormId) throws Exception {
        try {
            List<DormRpAssociationVo> dormRpAssociationVos = this.service.getDormViolateBydormId(dormId);
            this.logSuccessString("getDormViolate", "��ѯ����Υ�����", "�ɹ���ѯ����Υ�����");
            return dormRpAssociationVos;
        } catch (Exception e) {
            this.logFailureString("getDormViolate", "��ѯ����Υ�����", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
		 * @author ������ 2011-11-8
		 * gainDorm �������Ż�ȡ������Ϣ
		 * @param dormNumber �����
		 * @throws Exception
		 * @return
		 */
    @RemoteMethod
    public DormVo gainDormByDormNumber(String dormNumber) throws Exception {
        try {
            DormVo dormVo = this.service.gainDormByDormNumber(dormNumber);
            this.logSuccessString("getDormViolate", "��ѯ������Ϣ", "�ɹ���ѯ������Ϣ");
            return dormVo;
        } catch (Exception e) {
            this.logFailureString("getDormViolate", "��ѯ������Ϣ", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
		 * @author ������ 2011-11-8
		 * handAllot �ֶ���������
		 * @param studentId ѧ��Id
		 * @param dormId ����Id
		 * @throws Exception
		 * @return
		 */
    @RemoteMethod
    public List<DormVo> handAllot(Long dormId, Long formerDormId, Long studentId) throws Exception {
        try {
            List<DormVo> dormVos = this.service.handAllot(dormId, formerDormId, studentId);
            this.logSuccessString("getDormViolate", "�ֶ���������", "�ɹ���������");
            return dormVos;
        } catch (Exception e) {
            this.logFailureString("getDormViolate", "�ֶ���������", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
		 * @author ����ǿ 2011-11-8
		 * gaindoabuildNumber ��ȡ����
		 * @throws Exception
		 * @return buildNumber
		 */
    @RemoteMethod
    public List<String> gaindoabuildNumber() throws Exception {
        try {
            List<String> buildNumber = this.service.getAllBuildNumber();
            this.logSuccessString("gaindoabuildNumber", "��ȡ����", "�ɹ���ȡ����");
            return buildNumber;
        } catch (Exception e) {
            this.logFailureString("gaindoabuildNumber", "��ȡ����", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
		 * @author ����ǿ 2011-11-8
		 * singleAddDrom �������ᵼ��
		 * @throws Exception
		 * @return buildNumber
		 */
    @RemoteMethod
    public DormVo singleAddDrom(Long collegeId, Long professionId, Long classId, String dormNumber, Integer bedNumber, Boolean sex, Boolean forbidden) throws Exception {
        try {
            DormVo dormVo = new DormVo();
            dormVo.setCollegeId(collegeId);
            dormVo.setProfessionId(professionId);
            dormVo.setClassId(classId);
            dormVo.setSex(sex);
            dormVo.setDormNumber(dormNumber);
            dormVo.setDormForbidden(forbidden);
            dormVo.setDormBedNumber(bedNumber);
            DormVo dormVo2 = this.service.singleAddDrom(dormVo);
            this.logSuccessString("singleAddDrom", "�������ᵼ��", "�ɹ���������");
            return dormVo2;
        } catch (Exception e) {
            this.logFailureString("singleAddDrom", "�������ᵼ��", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
		 * @author ������ 2011-11-9
		 * updataViolate ����Υ�͵��޸�
		 * @param rpName Υ������
		 * @param rpId Υ������Id
		 * @param dormId ����Id
		 * @throws Exception
		 * @return
		 */
    @RemoteMethod
    public DormVo singleUpdataDorm(Long dormId, Long collegeId, Long professionId, Long classId, Boolean sex) throws Exception {
        try {
            DormVo dormVo = new DormVo();
            dormVo.setCollegeId(collegeId);
            dormVo.setProfessionId(professionId);
            dormVo.setClassId(classId);
            dormVo.setSex(sex);
            dormVo.setDormId(dormId);
            DormVo dormVo2 = this.service.singleUpdataDorm(dormVo);
            this.logSuccessString("singleAddDrom", "���µ������ᵼ��", "�ɹ��������ᵼ��");
            return dormVo2;
        } catch (Exception e) {
            this.logFailureString("singleAddDrom", "���µ������ᵼ��", e);
        }
        return null;
    }

    public DormRpAssociationVo updataDormViolate(String time, String rpName, Long rpId, Long dormId) throws Exception {
        try {
            DormRpAssociationVo dormRpAssociationVo = new DormRpAssociationVo();
            dormRpAssociationVo.setDormId(dormId);
            dormRpAssociationVo.setRpId(rpId);
            dormRpAssociationVo.setRpName(rpName);
            dormRpAssociationVo.setDormRpTime(time);
            DormRpAssociationVo dormRpAssociationVo2 = this.service.updataDormViolate(dormRpAssociationVo);
            this.logSuccessString("updataViolate", "�޸�����Υ������", "�ɹ��޸�����Υ������");
            return dormRpAssociationVo2;
        } catch (Exception e) {
            this.logFailureString("updataViolate", "�޸�����Υ������", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
		 * @author ������ 2011-11-9
		 * increaseViolate ����Υ�͵�����
		  * @param rpName Υ������
		 * @param rpId Υ������Id
		 * @param dormId ����Id
		 * @throws Exception
		 * @return
		 */
    @RemoteMethod
    public DormRpAssociationVo increaseDormViolate(String rpName, Long dormId) throws Exception {
        try {
            DormRpAssociationVo dormRpAssociationVo = new DormRpAssociationVo();
            dormRpAssociationVo.setDormId(dormId);
            dormRpAssociationVo.setRpName(rpName);
            DormRpAssociationVo dormRpAssociationVo2 = this.service.increaseDormViolate(dormRpAssociationVo);
            this.logSuccessString("increaseViolate", "��������Υ������", "�ɹ���������Υ������");
            return dormRpAssociationVo2;
        } catch (Exception e) {
            this.logFailureString("increaseViolate", "��������Υ������", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
		 * @author ������ 2011-11-10
		 * deleteViolate ����Υ�͵�ɾ��
		 * @param rpId Υ������Id
		 * @param dormId ����Id
		 * @throws Exception
		 * @return
		 */
    @RemoteMethod
    public Boolean deleteViolate(Long rpId, Long dormId) throws Exception {
        try {
            Boolean bl = this.service.deleteViolate(rpId, dormId);
            this.logSuccessString("deleteViolate", "ɾ������Υ��", "�ɹ�ɾ������Υ��");
            return bl;
        } catch (Exception e) {
            this.logFailureString("deleteViolate", "ɾ������Υ��", e);
            e.printStackTrace();
        }
        return null;
    }
}
