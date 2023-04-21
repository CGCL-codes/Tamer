package com.city.itis.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.annotation.Resource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;
import com.city.itis.domain.Hobby;
import com.city.itis.domain.Member;
import com.city.itis.domain.MemberCategory;
import com.city.itis.service.HobbyService;
import com.city.itis.service.MemberCategoryService;
import com.city.itis.service.MemberService;
import com.city.itis.service.SiteService;
import com.city.itis.util.Constants;
import com.city.itis.util.PopupAuthenticator;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员Action
 * @author WY
 *
 */
@Controller
public class MemberAction extends ActionSupport implements ModelDriven<Member> {

    private static final long serialVersionUID = 1L;

    private List<Member> memberList = null;

    private Member member = new Member();

    private String command = null;

    private MemberService memberService;

    private MemberCategoryService memberCategoryService;

    private List<MemberCategory> memberCategoryList = null;

    private HobbyService hobbyService;

    private SiteService siteService;

    private List<Hobby> hobbyList = null;

    private String dealPhoto = null;

    private File photo;

    private String photoFileName;

    private String photoContentType;

    Map<String, Object> map = null;

    private String url = null;

    private String msg = null;

    private List<Integer> hobbyIds = new ArrayList<Integer>();

    private Set<Hobby> hobbys = null;

    private int hids[] = null;

    private String result = null;

    /**
	 * 显示添加方法
	 * @return
	 */
    public String show_add() throws Exception {
        memberCategoryList = memberCategoryService.findAll();
        hobbyList = hobbyService.findAll();
        if (memberCategoryList != null && hobbyList != null) {
            url = "/member/member_add.jsp";
            return SUCCESS;
        }
        return ERROR;
    }

    /**
	 * 添加方法
	 * @return
	 */
    public String add() throws Exception {
        member.setPhotoName(photoFileName);
        member.setSignupDate(new Date());
        member.getCategory().setId(member.getMemberCategoryNo());
        hobbys = new HashSet<Hobby>();
        for (Integer id : hobbyIds) {
            Hobby hobby = hobbyService.getHobbyById(id);
            hobbys.add(hobby);
        }
        member.setHobbys(hobbys);
        int flag = memberService.add(member);
        if (flag > 0) {
            if (member.getPhotoName() != null) {
                save_photo();
            }
            url = "/member/list.action";
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    /**
	 * 修改方法
	 * @return
	 */
    public String modify() throws Exception {
        if (dealPhoto.equals(Constants.MODIFY)) {
            save_photo();
            member.setPhotoName(photoFileName);
        } else if (dealPhoto.equals(Constants.DELETE)) {
            delete_photo();
            member.setPhotoName(null);
        } else {
            member.setPhotoName(member.getPhotoName());
        }
        Integer memberCategoryNo = member.getMemberCategoryNo();
        if (memberCategoryNo != null) {
            Member m = memberService.getMemberById(member.getId());
            if (!memberCategoryNo.equals(m.getCategory().getId())) {
                member.setLevel(1);
            } else {
                member.setLevel(m.getLevel());
            }
            hobbys = new HashSet<Hobby>();
            for (Integer id : hobbyIds) {
                Hobby hobby = hobbyService.getHobbyById(id);
                hobbys.add(hobby);
            }
            member.setHobbys(hobbys);
            member.getCategory().setId(memberCategoryNo);
            int flag = memberService.modify(member);
            if (flag > 0) {
                url = "/member/list.action";
                return SUCCESS;
            } else {
                return ERROR;
            }
        } else {
            Member m = memberService.getMemberById(member.getId());
            hobbys = new HashSet<Hobby>();
            for (Integer id : hobbyIds) {
                Hobby hobby = hobbyService.getHobbyById(id);
                hobbys.add(hobby);
            }
            member.setHobbys(hobbys);
            member.getCategory().setId(m.getCategory().getId());
            int flag = memberService.modify(member);
            if (flag > 0) {
                url = "/member/detail.action";
                return SUCCESS;
            } else {
                return ERROR;
            }
        }
    }

    /**
	 * 删除方法
	 * @return
	 */
    public String delete() {
        try {
            member.getCategory().setId(member.getMemberCategoryNo());
            siteService.deleteByMemberId(member.getId());
            int flag = memberService.delete(member);
            if (flag > 0) {
                if (member.getPhotoName() != null) {
                    delete_photo();
                }
                url = "/member/list.action";
                return SUCCESS;
            } else {
                return ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NONE;
    }

    /**
	 * 根据用户编号，查询用户信息
	 * @return
	 */
    public String find() throws Exception {
        memberCategoryList = memberCategoryService.findAll();
        member = memberService.getMemberById(member.getId());
        if (member.getHobbys() != null) {
            hids = new int[member.getHobbys().size()];
            int i = 0;
            for (Hobby h : member.getHobbys()) {
                hids[i] = h.getId();
                i++;
            }
        }
        hobbyList = hobbyService.findAll();
        if (memberCategoryList != null && member != null && hobbyList != null) {
            if (Constants.MODIFY.equals(command)) {
                url = "/member/member_modify.jsp";
                return SUCCESS;
            } else if (Constants.DELETE.equals(command)) {
                url = "/member/member_delete.jsp";
                return SUCCESS;
            } else if (Constants.DETAIL.equals(command)) {
                url = "/member/member_detail.jsp";
                return SUCCESS;
            } else {
                return NONE;
            }
        }
        return ERROR;
    }

    public String getMemberByMemberId() throws Exception {
        Member m = memberService.getMemberByMemberId(member.getMemberId());
        if (m != null) {
            result = "用户账号已经存在，请使用别的账号注册。";
        } else {
            result = "恭喜您，该账号可用。";
        }
        return SUCCESS;
    }

    /**
	 * 根据用户编号，查询用户信息
	 * @return
	 */
    public String detail() throws Exception {
        map = ActionContext.getContext().getSession();
        Member m = (Member) map.get("login_user");
        Integer id = m.getId();
        member = memberService.getMemberById(id);
        if (member != null) {
            hobbyList = hobbyService.findAll();
            if (member.getHobbys() != null) {
                hids = new int[member.getHobbys().size()];
                int i = 0;
                for (Hobby h : member.getHobbys()) {
                    hids[i] = h.getId();
                    i++;
                }
            }
            url = "/member/member_detail.jsp";
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    /**
	 * 查询所有用户信息
	 * @return
	 */
    public String list() throws Exception {
        memberList = memberService.findAll();
        if (memberList != null) {
            url = "/member/member_maint.jsp";
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    /**
     * 保存图片方法
     */
    public void save_photo() throws Exception {
        String realPath = ServletActionContext.getServletContext().getRealPath("/upload");
        if (photo != null) {
            File saveFile = new File(new File(realPath), photoFileName);
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }
            FileUtils.copyFile(photo, saveFile);
        }
    }

    /**
	 * 删除图片方法
	 */
    public void delete_photo() throws Exception {
        String realPath = ServletActionContext.getServletContext().getRealPath("/upload");
        if (member.getPhotoName() != null) {
            File deleteFile = new File(new File(realPath), member.getPhotoName());
            if (!deleteFile.getParentFile().exists()) {
                deleteFile.getParentFile().mkdirs();
            }
            if (deleteFile != null) {
                deleteFile.delete();
            }
        }
    }

    /**
	 * 会员注册方法
	 * @return
	 * @throws Exception
	 */
    public String register() throws Exception {
        member.setPhotoName(photoFileName);
        member.setSignupDate(new Date());
        hobbys = new HashSet<Hobby>();
        for (Integer id : hobbyIds) {
            Hobby hobby = hobbyService.getHobbyById(id);
            hobbys.add(hobby);
        }
        member.setHobbys(hobbys);
        member.setHobbys(hobbys);
        member.getCategory().setId(1);
        member.setLevel(1);
        int flag = memberService.add(member);
        if (flag > 0) {
            if (member.getPhotoName() != null) {
                save_photo();
            }
            msg = "恭喜您，你已经添加成我们的会员。赶快去登陆本系统吧！！！";
            url = "/member/member_alert.jsp";
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    /**
	 * 会员注册确认方法
	 * @return
	 * @throws Exception
	 */
    public String confirm_register() throws Exception {
        return SUCCESS;
    }

    public void send_mail() throws Exception {
        String username = "stark_summer@sina.com";
        String password = "stark_summer";
        String smtp_server = "smtp.sina.com";
        String from_mail_address = username;
        String to_mail_address = member.getEmail();
        PopupAuthenticator auth = new PopupAuthenticator(username, password);
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.auth", "true");
        mailProps.put("username", username);
        mailProps.put("password", password);
        mailProps.put("mail.smtp.host", smtp_server);
        Session mailSession = Session.getDefaultInstance(mailProps, auth);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(from_mail_address));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to_mail_address));
        message.setSubject("Mail Test");
        MimeMultipart multi = new MimeMultipart();
        BodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setText("电子邮件测试内容!");
        multi.addBodyPart(textBodyPart);
        message.setContent(multi);
        message.saveChanges();
        Transport.send(message);
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public MemberService getMemberService() {
        return memberService;
    }

    @Resource
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    public String getDealPhoto() {
        return dealPhoto;
    }

    public void setDealPhoto(String dealPhoto) {
        this.dealPhoto = dealPhoto;
    }

    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    @Override
    public Member getModel() {
        return member;
    }

    public MemberCategoryService getMemberCategoryService() {
        return memberCategoryService;
    }

    @Resource
    public void setMemberCategoyrService(MemberCategoryService memberCategoryService) {
        this.memberCategoryService = memberCategoryService;
    }

    public List<MemberCategory> getMemberCategoryList() {
        return memberCategoryList;
    }

    public void setMemberCategoryList(List<MemberCategory> memberCategoryList) {
        this.memberCategoryList = memberCategoryList;
    }

    @Resource
    public void setMemberCategoryService(MemberCategoryService memberCategoryService) {
        this.memberCategoryService = memberCategoryService;
    }

    public HobbyService getHobbyService() {
        return hobbyService;
    }

    @Resource
    public void setHobbyService(HobbyService hobbyService) {
        this.hobbyService = hobbyService;
    }

    public List<Hobby> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<Hobby> hobbyList) {
        this.hobbyList = hobbyList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Integer> getHobbyIds() {
        return hobbyIds;
    }

    public void setHobbyIds(List<Integer> hobbyIds) {
        this.hobbyIds = hobbyIds;
    }

    public int[] getHids() {
        return hids;
    }

    public void setHids(int[] hids) {
        this.hids = hids;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public SiteService getSiteService() {
        return siteService;
    }

    @Resource
    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
