package cn.jsprun.struts.foreg.actions;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import cn.jsprun.domain.Admingroups;
import cn.jsprun.domain.Forumfields;
import cn.jsprun.domain.Forums;
import cn.jsprun.domain.Members;
import cn.jsprun.domain.Posts;
import cn.jsprun.domain.Threads;
import cn.jsprun.domain.Usergroups;
import cn.jsprun.foreg.utils.Jspruncode;
import cn.jsprun.service.forumsedit.ForumService;
import cn.jsprun.service.forumsedit.ForumfieldService;
import cn.jsprun.service.posts.PostsService;
import cn.jsprun.service.posts.ThreadsService;
import cn.jsprun.service.system.DataBaseService;
import cn.jsprun.service.user.MemberService;
import cn.jsprun.service.user.UserGroupService;
import cn.jsprun.utils.BeanFactory;
import cn.jsprun.utils.Common;
import cn.jsprun.utils.ForumInit;
import cn.jsprun.utils.IPSeeker;
import cn.jsprun.utils.JspRunConfig;

public class ModcpAction extends DispatchAction {

    private DataBaseService dataBaseService = (DataBaseService) BeanFactory.getBean("dataBaseService");

    private UserGroupService userGroupService = (UserGroupService) BeanFactory.getBean("userGroupService");

    private PostsService postService = (PostsService) BeanFactory.getBean("postsService");

    private ThreadsService threadService = (ThreadsService) BeanFactory.getBean("threadsService");

    private ForumfieldService forumfieldService = (ForumfieldService) BeanFactory.getBean("forumfieldService");

    private ForumService forumService = (ForumService) BeanFactory.getBean("forumService");

    private MemberService memberService = (MemberService) BeanFactory.getBean("memberService");

    @SuppressWarnings("unchecked")
    public ActionForward editsubject(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Program", "no-cache");
        response.setDateHeader("Expirse", 0);
        int tid = Common.toDigit(request.getParameter("tid"), 1000000000L, 0L).intValue();
        short fid = Common.toDigit(request.getParameter("fid"), 1000000000L, 0L).shortValue();
        List<Map<String, String>> orig = dataBaseService.executeQuery("SELECT m.adminid,p.subject,p.first, p.authorid, p.author, p.dateline, p.anonymous, p.invisible FROM jrun_posts p LEFT JOIN jrun_members m ON m.uid=p.authorid WHERE p.tid='" + tid + "' AND p.first='1' limit 1", new String[] { "adminid", "subject", "first", "authorid", "author", "dateline", "anonymous", "invisible" });
        if (orig == null || orig.size() <= 0) {
            this.showMessage("ajaxerror" + new Random().nextInt(1000), "ָ�������ⲻ���ڻ��ѱ�ɾ������ڱ���ˣ��뷵�ء�", response);
            return null;
        }
        Map<String, String> post = orig.get(0);
        HttpSession session = request.getSession();
        short groupid = (Short) session.getAttribute("jsprun_groupid");
        Members member = (Members) session.getAttribute("user");
        Map<String, String> settings = ForumInit.settings;
        Usergroups usergroup = userGroupService.findUserGroupById(member.getGroupid());
        String message = Common.periodscheck(settings.get("postbanperiods"), usergroup.getDisableperiodctrl(), Float.valueOf(settings.get("timeoffset")));
        settings = null;
        if (message != null) {
            this.showMessage("ajaxerror", message, response);
            return null;
        }
        Forumfields forumfield = forumfieldService.findById(fid);
        if ((forumfield.getViewperm() == null || forumfield.getViewperm().equals("")) && usergroup.getReadaccess() <= 0) {
            this.showMessage("ajaxerror", "�����ڵ��û���(" + usergroup.getGrouptitle() + ")�޷����д˲�����", response);
            return null;
        } else if ((!forumfield.getViewperm().equals("")) && !Common.forumperm(forumfield.getViewperm(), groupid, member != null ? member.getExtgroupids() : "")) {
            this.showMessage("ajaxerror", "�����ֻ���ض��û�����Է��ʡ�", response);
            return null;
        }
        forumfield = null;
        boolean ismoderator = Common.ismoderator(fid, member);
        int adminid = Common.toDigit(post.get("adminid"), 255L, 0L).intValue();
        Admingroups admingroup = userGroupService.findAdminGroupById(member.getGroupid());
        if (!ismoderator || admingroup == null || admingroup.getAlloweditpost() <= 0 || ((adminid == 1 || adminid == 2 || adminid == 3) && member.getAdminid() > adminid)) {
            this.showMessage("ajaxerror", "�Բ�����û��Ȩ���༭���˷�������ӣ��뷵�ء�", response);
            return null;
        }
        String subjectnew = request.getParameter("subjectnew");
        if (subjectnew != null) {
            subjectnew = Common.ajax_decode(subjectnew);
        }
        if (subjectnew != null && Common.strlen(subjectnew) > 80) {
            this.showMessage("ajaxerror", "�Բ�����ı��ⳬ�� 80 ���ַ��뷵���޸ı��ⳤ�ȡ�", response);
            return null;
        }
        if (request.getParameter("editsubjectsubmit") == null) {
            request.setAttribute("tid", tid);
            request.setAttribute("fid", fid);
            request.setAttribute("subject", post.get("subject"));
            return mapping.findForward("tomodcppost");
        } else {
            subjectnew = Common.dhtmlspecialchars(subjectnew);
            dataBaseService.runQuery("UPDATE jrun_threads SET subject='" + subjectnew + "' WHERE tid='" + tid + "'");
            dataBaseService.runQuery("UPDATE jrun_posts SET subject='" + subjectnew + "' WHERE tid='" + tid + "' AND first='1'");
            this.showMessage("<a href=\"viewthread.jsp?tid=" + tid + "\">" + subjectnew + "</a>", null, response);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public ActionForward editmessage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String editmessagesubmit = request.getParameter("editmessagesubmit");
        HttpSession session = request.getSession();
        String message = "";
        int jrun_uid = (Integer) session.getAttribute("jsprun_uid");
        Members member = (Members) session.getAttribute("user");
        if (jrun_uid == 0) {
            message = "δ��¼���޷����в�����";
            this.showmessage(false, message, response);
            return null;
        }
        String pid = request.getParameter("pid");
        List<Posts> postlist = postService.findPostByhql("from Posts as p where p.pid=" + pid + " and p.invisible>-1", 0, 1);
        if (postlist == null || postlist.size() <= 0) {
            message = "ָ�������Ӳ����ڻ��ѱ�ɾ������ڱ���ˡ�<br /><br /><a href=\"viewthread.jsp?tid=" + request.getParameter("tid") + "\">[ ����ת�������������� ]</a>";
            this.showmessage(false, message, response);
            return null;
        }
        Map<String, String> settings = ForumInit.settings;
        short groupid = (Short) session.getAttribute("jsprun_groupid");
        Usergroups usergroups = userGroupService.findUserGroupById(groupid);
        String showmessage = Common.periodscheck(settings.get("postbanperiods"), usergroups.getDisableperiodctrl(), Float.valueOf(settings.get("timeoffset")));
        if (showmessage != null) {
            this.showMessage("ajaxerror", showmessage, response);
            return null;
        }
        Posts post = postlist.get(0);
        String activitsql = "SELECT f.fid, f.name, ff.viewperm, a.allowview FROM jrun_forums f LEFT JOIN jrun_forumfields ff ON ff.fid=f.fid LEFT JOIN jrun_access a ON a.uid='" + jrun_uid + "' AND a.fid=f.fid WHERE f.status>0  and f.fid = " + post.getFid() + " ORDER BY f.displayorder";
        List<Map<String, String>> forumslist = dataBaseService.executeQuery(activitsql);
        if (forumslist.size() <= 0) {
            message = "δ����������뷵�ء�";
            this.showmessage(false, message, response);
            return null;
        }
        Map<String, String> forumMap = forumslist.get(0);
        String viewperm = forumMap.get("viewperm");
        if (forumMap.get("alloview") == null) {
            if (forumMap.get("viewperm").equals("") && usergroups.getReadaccess() == 0) {
                message = "�����ڵ��û���(" + usergroups.getGrouptitle() + ")�޷����д˲�����";
                this.showmessage(false, message, response);
                return null;
            } else if (!forumMap.get("viewperm").equals("") && !Common.forumperm(viewperm, groupid, member != null ? member.getExtgroupids() : "")) {
                message = "�����ֻ���ض��û�����Է��ʡ�";
                this.showmessage(false, message, response);
                return null;
            }
        }
        boolean modertar = Common.ismoderator(post.getFid(), member);
        byte jsprun_adminid = (Byte) session.getAttribute("jsprun_adminid");
        Admingroups admingroup = userGroupService.findAdminGroupById(member.getGroupid());
        Forums forums = forumService.findById(Short.valueOf(forumMap.get("fid")));
        if (forums == null) {
            request.setAttribute("errorInfo", "ָ���İ�鲻���ڣ��뷵�ء�");
            return mapping.findForward("showMessage");
        }
        if ((admingroup == null || admingroup.getAlloweditpost() == 0 || forums.getAlloweditpost() == 0 || !modertar) && jrun_uid != post.getAuthorid()) {
            message = "�Բ�����û��Ȩ���༭���˷�������ӣ��뷵�ء�";
            this.showmessage(false, message, response);
            return null;
        }
        Members author = memberService.findMemberById(post.getAuthorid());
        if (author != null && (author.getAdminid() > 0 && jsprun_adminid > author.getAdminid())) {
            message = "�Բ�����û��Ȩ���༭���˷�������ӣ��뷵�ء�";
            this.showmessage(false, message, response);
            return null;
        }
        if (editmessagesubmit == null) {
            request.setAttribute("post", post);
            return mapping.findForward("tomodcppost");
        } else {
            List<Map<String, String>> smilieslist = dataBaseService.executeQuery("select s.id,s.typeid,s.code,s.url,i.directory from jrun_smilies s left join jrun_imagetypes  i on s.typeid=i.typeid where s.type='smiley' order by s.displayorder");
            String dos = request.getParameter("do");
            String parnum = settings.get("maxsmilies");
            if (dos != null) {
                Posts posts = postService.getPostsById(convertInt(pid));
                String messages = posts.getMessage();
                messages = this.parseSmilies(smilieslist, messages, convertInt(parnum), forums.getAllowimgcode() > 0);
                try {
                    response.getWriter().write(messages);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String messages = request.getParameter("message");
                message = message.replace("\\", "\\\\");
                dataBaseService.runQuery("update jrun_posts set message='" + messages.replace("'", "''") + "' where pid = " + pid);
                messages = this.parseSmilies(smilieslist, messages, convertInt(parnum), forums.getAllowimgcode() > 0);
                writeMessage(response, messages, false);
            }
            smilieslist = null;
            return null;
        }
    }

    public ActionForward getip(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Program", "no-cache");
        response.setDateHeader("Expirse", 0);
        String pid = request.getParameter("pid");
        String tid = request.getParameter("tid");
        HttpSession session = request.getSession();
        short groupid = (Short) session.getAttribute("jsprun_groupid");
        byte jsprun_adminid = (Byte) (session.getAttribute("jsprun_adminid") == null ? 0 : session.getAttribute("jsprun_adminid"));
        String modertarhql = "SELECT m.adminid, p.first, m.regip FROM jrun_posts p	LEFT JOIN jrun_members m ON m.uid=p.authorid WHERE pid='" + pid + "' AND tid='" + tid + "'";
        List<Map<String, String>> members = dataBaseService.executeQuery(modertarhql);
        String message = "";
        if (members == null || members.size() <= 0) {
            message = "ָ�������ⲻ���ڻ��ѱ�ɾ������ڱ���ˣ��뷵�ء�";
            this.showMessage("ajaxerror", message, response);
            return null;
        } else {
            Threads thread = threadService.findByTid(convertInt(tid));
            Map<String, String> membermap = members.get(0);
            if (("1".equals(membermap.get("adminid")) && jsprun_adminid > 1) || ("2".equals(membermap.get("adminid")) && jsprun_adminid > 2)) {
                message = "�Բ�����û��Ȩ�޲鿴����߼��Ĺ���Ա IP���뷵�ء�";
                this.showMessage("ajaxerror", message, response);
                return null;
            } else if (membermap.get("first").equals("1") && thread.getDigest() == -1) {
                message = "�������ⲻ������д˲������뷵�ء�";
                this.showMessage("ajaxerror", message, response);
                return null;
            }
            IPSeeker seeker = IPSeeker.getInstance();
            String address = seeker.getAddress(membermap.get("regip") == null ? "" : membermap.get("regip"));
            request.setAttribute("address", address);
            request.setAttribute("ip", membermap.get("regip"));
            Admingroups admingroup = userGroupService.findAdminGroupById(Short.valueOf(groupid + ""));
            if (admingroup != null) {
                request.setAttribute("banip", admingroup.getAllowbanip());
            }
            return mapping.findForward("topicagetip");
        }
    }

    private void writeMessage(HttpServletResponse response, String message, boolean iserror) {
        response.setContentType("application/xml");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Program", "no-cache");
        response.setDateHeader("Expirse", 0);
        String content = "<?xml version=\"1.0\" encoding=\"" + JspRunConfig.charset + "\"?><root><![CDATA[";
        if (iserror) {
            message = message + " <script type=\"text/javascript\" reload=\"1\">function ajaxerror() { alert('" + message + "');}ajaxerror();</script>";
        }
        message = message.replaceAll("([\\x01-\\x09\\x0b-\\x0c\\x0e-\\x1f])+", "");
        message = message.replaceAll("]]>", "]]&gt");
        content = content + message + "]]></root>";
        try {
            response.getWriter().write(content);
        } catch (IOException e) {
        }
    }

    @SuppressWarnings("unused")
    private int convertInt(String s) {
        int count = 0;
        try {
            count = Integer.valueOf(s);
        } catch (Exception e) {
        }
        return count;
    }

    private String parseSmilies(List<Map<String, String>> smilieslist, String message, int parnum, boolean allowimgcode) {
        message = message.replace("$", "javadanglefuhao") + " ";
        Jspruncode jspcode = (Jspruncode) BeanFactory.getBean("jspruncode");
        message = jspcode.parsecode(message, false);
        message = jspcode.parsetable(message);
        message = jspcode.parseimg(message, allowimgcode);
        message = message.replaceAll("\\n", "<br/>");
        message = message.replaceAll("(?i)<br/>(<TD>|<TR>|</TR>)", "$1");
        message = relacesmile(smilieslist, message, parnum);
        message = message.replace("javadanglefuhao", "$");
        return message;
    }

    private String relacesmile(List<Map<String, String>> smilieslist, String message, int parnum) {
        for (Map<String, String> smiles : smilieslist) {
            if (message.indexOf(smiles.get("code") + " ") != -1 || message.indexOf(" " + smiles.get("code")) != -1) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("<img src='images/smilies/");
                buffer.append(smiles.get("directory"));
                buffer.append("/");
                buffer.append(smiles.get("url"));
                buffer.append("' smilieid='");
                buffer.append(smiles.get("id"));
                buffer.append("' border='0' alt='' /> ");
                message = StringUtils.replace(message, smiles.get("code"), buffer.toString(), parnum);
            }
        }
        return message;
    }

    private void showmessage(boolean sucess, String message, HttpServletResponse response) {
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Program", "no-cache");
        response.setDateHeader("Expirse", 0);
        try {
            if (sucess) {
                response.getWriter().write(message);
            } else {
                response.getWriter().write("error:" + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String url, String message, HttpServletResponse response) {
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Program", "no-cache");
        response.setDateHeader("Expirse", 0);
        try {
            if (message != null) {
                response.getWriter().write(url + "<script type=\"text/javascript\">alert('" + message + "����״̬��:" + new Random().nextInt(10000) + "');</script>");
            } else {
                response.getWriter().write(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
