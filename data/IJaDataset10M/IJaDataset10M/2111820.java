package cn.jsprun.dao.posts;

import java.util.List;
import cn.jsprun.domain.Attachments;
import cn.jsprun.struts.form.posts.AttachmentsForm;

public interface AttachmentsDao {

    public String findByAttachmentsForm(AttachmentsForm attachmentsForm);

    public Integer deleteArray(String[] aids);

    public List<Attachments> findByPostsID(Integer pid);

    public List<Attachments> findAttchmentsByJs(String hql, int startrow, int maxrow);

    public Attachments findAttachmentsById(int aid);

    public int insertAttachments(Attachments attachments);

    public boolean updateAttachments(Attachments attachments);

    public boolean deleteAttachments(Attachments attachments);

    public List<Attachments> findByattaByTid(Integer tid);

    public List<Attachments> getAttachmentListByTid(Integer tid);

    public void updateAttachment(List<Attachments> attachmentsList);
}
