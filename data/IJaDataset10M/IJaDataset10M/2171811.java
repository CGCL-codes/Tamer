package com.student.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.student.dao.FillDataDao;
import com.student.dao.StudentInfDao;
import com.student.model.ParentInfo;
import com.student.model.PoliticsStatus;
import com.student.model.Student;
import com.student.util.Model2VoUtil;
import com.student.vo.StudentVo;

/**
 * ѧ�����Ϣ
 * studentInfoService
 * @Author:������
 *@author Administrator
 *@createTime:2011-11-1 ����21��37
 *@version 1.0.0
 */
@Component("studentInfoService")
public class StudentInfoService {

    private StudentInfDao studentInfDao;

    private FillDataDao PoliticsStatusInfos;

    /**
 * 
 * getStudentinf(����ѧ�����Ϣ�������Ǹ��ѧ��id���в��ң�����ѧ��id�����ж��Ƿ���ڣ���ȥ������Ϣ)
 * @param Sid
 * @return
 * @throws Exception 
 *@return List<Student>
 * @exception 
 * @since  1.0
 */
    public StudentInfDao getStudentInfDao() {
        return studentInfDao;
    }

    @Resource
    public void setStudentInfDao(StudentInfDao studentInfDao) {
        this.studentInfDao = studentInfDao;
    }

    public FillDataDao getPoliticsStatusInfos() {
        return PoliticsStatusInfos;
    }

    @Resource
    public void setPoliticsStatusInfos(FillDataDao politicsStatusInfos) {
        PoliticsStatusInfos = politicsStatusInfos;
    }

    public StudentVo getStudentinf(Long studentId) throws Exception {
        Student student = this.studentInfDao.getStudentinfById(studentId);
        List<ParentInfo> parentInfos = this.studentInfDao.loadFamilyInfo(studentId);
        StudentVo vo = Model2VoUtil.student2StudentVo(student);
        List<PoliticsStatus> plolitics = PoliticsStatusInfos.loadAllPolitics();
        vo.setFamilyInfoList(Model2VoUtil.family2FamilyVo(parentInfos));
        Long b = vo.getPoliticsStatusId();
        System.out.println(parentInfos.get(0).getParentName());
        return vo;
    }

    /**
 * 	
 * updateStudent(���ڸ���ѧ����Ϣ�������ǽ��б����������֤���ٰ���Ӧ�������д��ȥ)
 * @param stud
 * @return
 * @throws Exception 
 *@return Student
 * @exception 
 * @since  1.0
 */
    public StudentVo updateStudent(Student stud) throws Exception {
        if (stud == null || stud.getStudentId() == null) {
            return null;
        }
        Student persistStu = this.studentInfDao.getStudentinfById(stud.getStudentId());
        persistStu.setZipcode(stud.getZipcode());
        Student student = this.studentInfDao.updateStudentinf(stud);
        return Model2VoUtil.student2StudentVo(persistStu);
    }
}
