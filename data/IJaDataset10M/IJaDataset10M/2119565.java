package com.student.dao;

import java.util.List;
import com.student.model.Student;
import com.student.util.PageInfo;

/**
 * @Description:ѧ�����DAO��
 * @author �¸���
 * @createTime:2011-11-11 15:20
 */
public interface StudentManageDao {

    /**
	 * @author �¸���
	 * @Description <getStudentListAll����> �ۺϲ�ѯѧ����Ϣ
	 * @param collegeId ѧԺId
	 * @param gradeId �꼶Id
	 * @param progessionId רҵId
	 * @param classId �༶Id
	 * @param studentNumber ѧ��
	 * @param name ѧ�����
	 * @param sex �Ա�
	 * @param politicsStatusId ������ò Id
	 * @param isInsure �Ƿ�α�
	 * @param isAllAttend �Ƿ�ȫ��
	 * @param orderType ����ʽ
	 * @param pageInfo ��ҳ
	 * @return
	 * @throws Exception
	 * @createTime:2011-11-11 15:20
	 */
    public List<Student> getStudentListAll(Long collegeId, Long gradeId, Long progessionId, Long classId, String studentNumber, String studentName, Boolean sex, Long politicsStatusId, Boolean isInsure, String isAllAttend, String orderType, PageInfo pageInfo) throws Exception;
}
