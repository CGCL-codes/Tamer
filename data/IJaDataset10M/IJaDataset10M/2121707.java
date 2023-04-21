package cn.vlabs.clb.api.folder;

import cn.vlabs.clb.api.AccessForbidden;
import cn.vlabs.clb.api.ResourceNotFound;

/**
 * Ŀ¼����
 * @author л����(xiejj@cnic.cn)
 * @created Mar 31, 2009
 */
public interface FolderService {

    /**
	 * ����Ŀ¼
	 * @param path
	 */
    public void mkdir(String path) throws ResourceNotFound, AccessForbidden;

    /**
	 * ɾ��Ŀ¼
	 * @param path
	 */
    public void rmdir(String path) throws ResourceNotFound, AccessForbidden;

    /**
	 * ö��Ŀ¼��Ŀ¼��
	 * @param path
	 * @return
	 */
    public FolderInfo[] list(String path) throws ResourceNotFound, AccessForbidden;

    /**
	 * ����һ���ļ���
	 * @param docid �ĵ�ID
	 * @param path	·��
	 * @return
	 */
    public void createFile(int docid, String path) throws ResourceNotFound, AccessForbidden;

    /**
	 * ����һ���ļ���
	 * @param docid	�ĵ�ID
	 * @param path	�ĵ���·��
	 * @param filename	�ڸ�λ���ϵ��ļ���
	 * @throws ResourceNotFound
	 * @throws AccessForbidden
	 */
    public void createFile(int docid, String path, String filename) throws ResourceNotFound, AccessForbidden;

    /**
	 * �ж�һ��·���Ƿ����
	 * @param path �����·��
	 * @return
	 */
    public boolean exist(String path);

    /**
	 * ɾ���ļ�
	 * @param path
	 */
    public void delete(String path, boolean recurisive) throws ResourceNotFound, AccessForbidden;

    /**
	 * �������ļ�������
	 * @param from
	 * @param to
	 */
    public void link(String from, String to) throws ResourceNotFound, AccessForbidden;

    /**
	 * ɾ��Ŀ¼��
	 * @param path
	 */
    public void unlink(String path) throws ResourceNotFound, AccessForbidden;
}
