package org.unlimit.flexbbs.service.entity.post;

import java.util.List;

public interface IPostService {

    /******
	 * ������޸Ļظ�
	 * @param post
	 */
    public void savePost(Object post);

    /*****
	 * ɾ��ظ�
	 * @param post
	 */
    public void deletePost(Object post);

    /*******
	 * ��˻ظ�
	 * @param post
	 */
    public void auditPost(Object post);

    /*******
	 * �������id��ѯ��ػظ�
	 * @param threadId
	 * @return
	 */
    public List getPostByThreadId(Object threadId);
}
