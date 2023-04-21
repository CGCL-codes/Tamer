package cn.vlabs.clb.api.tag;

/**
 * ���еı�ǩ����
 * @author л����(xiejj@cnic.cn)
 * @created Apr 7, 2009
 */
public interface TagService {

    /**
	 * ���±�ǩ
	 * @param docid �ĵ�ID
	 * @param tags	�ĵ��ı�ǩ
	 */
    void updateTag(int docid, String[] tags);

    /**
	 * ��ѯ������еı�ǩ
	 * @return ���еı�ǩ
	 */
    String[] getAll();
}
