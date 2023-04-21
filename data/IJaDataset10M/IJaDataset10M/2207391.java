package net.infordata.ifw2.web.jsp7;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import net.infordata.ifw2.web.view.RendererContext;
import net.infordata.ifw2.web.TaskBoxFlow;
import net.infordata.ifw2.web.util.WEBUtil;
import net.infordata.ifw2.web.ctrl.TaskFuture;
import net.infordata.ifw2.web.ctrl.TaskStep;
import net.infordata.ifw2.web.ctrl.TaskInfo;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import net.infordata.ifw2.msgs.MessageTypeEnum;

@SuppressWarnings("all")
public final class TaskBox_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

    private static final javax.servlet.jsp.JspFactory _jspxFactory = javax.servlet.jsp.JspFactory.getDefaultFactory();

    private static java.util.List<java.lang.String> _jspx_dependants;

    private javax.el.ExpressionFactory _el_expressionfactory;

    private org.apache.tomcat.InstanceManager _jsp_instancemanager;

    public java.util.List<java.lang.String> getDependants() {
        return _jspx_dependants;
    }

    public void _jspInit() {
        _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
    }

    public void _jspDestroy() {
    }

    public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException {
        final javax.servlet.jsp.PageContext pageContext;
        javax.servlet.http.HttpSession session = null;
        final javax.servlet.ServletContext application;
        final javax.servlet.ServletConfig config;
        javax.servlet.jsp.JspWriter out = null;
        final java.lang.Object page = this;
        javax.servlet.jsp.JspWriter _jspx_out = null;
        javax.servlet.jsp.PageContext _jspx_page_context = null;
        try {
            response.setContentType("text/html");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write("\r\n\r\n\r\n");
            final TaskBoxFlow<?> flow = (TaskBoxFlow<?>) RendererContext.get().getCurrentFlow();
            final TaskFuture<?> future = flow.getTaskFuture();
            final TaskInfo taskInfo = future == null ? null : future.getTaskInfo();
            TaskStep[] steps = taskInfo == null ? new TaskStep[0] : taskInfo.getTaskSteps();
            final int pageSize = flow.getPageSize();
            if (future != null && future.isDone()) {
                try {
                    future.get();
                } catch (CancellationException ex) {
                    TaskStep[] sts = new TaskStep[steps.length + 1];
                    System.arraycopy(steps, 0, sts, 0, steps.length);
                    sts[sts.length - 1] = new TaskStep("Cancelled", null, -1);
                    steps = sts;
                } catch (InterruptedException ex) {
                    TaskStep[] sts = new TaskStep[steps.length + 1];
                    System.arraycopy(steps, 0, sts, 0, steps.length);
                    sts[sts.length - 1] = new TaskStep("Interrupted", ex.getMessage(), MessageTypeEnum.WARNING, -1);
                    steps = sts;
                } catch (ExecutionException ex) {
                    TaskStep[] sts = new TaskStep[steps.length + 1];
                    System.arraycopy(steps, 0, sts, 0, steps.length);
                    sts[sts.length - 1] = new TaskStep("Error", ex.getCause().getMessage(), MessageTypeEnum.ERROR, -1);
                    steps = sts;
                }
            }
            out.write("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n<table class=\"taskBox\" width=\"100%\">\r\n  ");
            net.infordata.ifw2.web.tags.TBodyTag _jspx_th_ifw2_005ftbody_005f0 = (new net.infordata.ifw2.web.tags.TBodyTag());
            _jsp_instancemanager.newInstance(_jspx_th_ifw2_005ftbody_005f0);
            _jspx_th_ifw2_005ftbody_005f0.setPageContext(_jspx_page_context);
            _jspx_th_ifw2_005ftbody_005f0.setParent(null);
            _jspx_th_ifw2_005ftbody_005f0.setId("tb");
            int _jspx_eval_ifw2_005ftbody_005f0 = _jspx_th_ifw2_005ftbody_005f0.doStartTag();
            if (_jspx_eval_ifw2_005ftbody_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ifw2_005ftbody_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.pushBody();
                    _jspx_th_ifw2_005ftbody_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                    _jspx_th_ifw2_005ftbody_005f0.doInitBody();
                }
                do {
                    out.write("\r\n    ");
                    net.infordata.ifw2.web.tags.TrTag _jspx_th_ifw2_005ftr_005f0 = (new net.infordata.ifw2.web.tags.TrTag());
                    _jsp_instancemanager.newInstance(_jspx_th_ifw2_005ftr_005f0);
                    _jspx_th_ifw2_005ftr_005f0.setPageContext(_jspx_page_context);
                    _jspx_th_ifw2_005ftr_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ifw2_005ftbody_005f0);
                    _jspx_th_ifw2_005ftr_005f0.setId("tra");
                    int _jspx_eval_ifw2_005ftr_005f0 = _jspx_th_ifw2_005ftr_005f0.doStartTag();
                    if (_jspx_eval_ifw2_005ftr_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ifw2_005ftr_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                            out = _jspx_page_context.pushBody();
                            _jspx_th_ifw2_005ftr_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                            _jspx_th_ifw2_005ftr_005f0.doInitBody();
                        }
                        do {
                            out.write("\r\n      <td colspan=\"3\" class=\"title\">      \r\n        ");
                            out.print(WEBUtil.nbsp(taskInfo == null ? null : taskInfo.getTitle()));
                            out.write("\r\n      </td>\r\n    ");
                            int evalDoAfterBody = _jspx_th_ifw2_005ftr_005f0.doAfterBody();
                            if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                        } while (true);
                        if (_jspx_eval_ifw2_005ftr_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                            out = _jspx_page_context.popBody();
                        }
                    }
                    if (_jspx_th_ifw2_005ftr_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _jspx_th_ifw2_005ftr_005f0.release();
                        _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005ftr_005f0);
                        return;
                    }
                    _jspx_th_ifw2_005ftr_005f0.release();
                    _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005ftr_005f0);
                    out.write("\r\n  ");
                    int i = Math.max(0, steps.length - pageSize);
                    for (; i < steps.length; i++) {
                        out.write("\r\n    ");
                        net.infordata.ifw2.web.tags.TrTag _jspx_th_ifw2_005ftr_005f1 = (new net.infordata.ifw2.web.tags.TrTag());
                        _jsp_instancemanager.newInstance(_jspx_th_ifw2_005ftr_005f1);
                        _jspx_th_ifw2_005ftr_005f1.setPageContext(_jspx_page_context);
                        _jspx_th_ifw2_005ftr_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ifw2_005ftbody_005f0);
                        _jspx_th_ifw2_005ftr_005f1.setId("tra" + i);
                        int _jspx_eval_ifw2_005ftr_005f1 = _jspx_th_ifw2_005ftr_005f1.doStartTag();
                        if (_jspx_eval_ifw2_005ftr_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ifw2_005ftr_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                                out = _jspx_page_context.pushBody();
                                _jspx_th_ifw2_005ftr_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                                _jspx_th_ifw2_005ftr_005f1.doInitBody();
                            }
                            do {
                                out.write("\r\n      <td colspan=\"3\" class=\"stepTitle\">      \r\n        ");
                                out.print(WEBUtil.nbsp(steps[i].getTitle()));
                                out.write("\r\n      </td>\r\n    ");
                                int evalDoAfterBody = _jspx_th_ifw2_005ftr_005f1.doAfterBody();
                                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                            } while (true);
                            if (_jspx_eval_ifw2_005ftr_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                                out = _jspx_page_context.popBody();
                            }
                        }
                        if (_jspx_th_ifw2_005ftr_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _jspx_th_ifw2_005ftr_005f1.release();
                            _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005ftr_005f1);
                            return;
                        }
                        _jspx_th_ifw2_005ftr_005f1.release();
                        _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005ftr_005f1);
                        out.write("\r\n    ");
                        net.infordata.ifw2.web.tags.TrTag _jspx_th_ifw2_005ftr_005f2 = (new net.infordata.ifw2.web.tags.TrTag());
                        _jsp_instancemanager.newInstance(_jspx_th_ifw2_005ftr_005f2);
                        _jspx_th_ifw2_005ftr_005f2.setPageContext(_jspx_page_context);
                        _jspx_th_ifw2_005ftr_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ifw2_005ftbody_005f0);
                        _jspx_th_ifw2_005ftr_005f2.setId("trb" + i);
                        int _jspx_eval_ifw2_005ftr_005f2 = _jspx_th_ifw2_005ftr_005f2.doStartTag();
                        if (_jspx_eval_ifw2_005ftr_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ifw2_005ftr_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                                out = _jspx_page_context.pushBody();
                                _jspx_th_ifw2_005ftr_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                                _jspx_th_ifw2_005ftr_005f2.doInitBody();
                            }
                            do {
                                out.write("\r\n      <td>\r\n        &nbsp;&nbsp;\r\n        ");
                                out.write("\r\n      </td>\r\n      <td width=\"100%\" class='");
                                out.print(steps[i].getMessageType() == null ? "" : steps[i].getMessageType().name().toLowerCase());
                                out.write("'>\r\n        ");
                                out.print(WEBUtil.nbsp(steps[i].getMessage()));
                                out.write("\r\n      </td>\r\n      <td width=\"80px\" align=\"right\">\r\n        ");
                                final int percentage = future.isDone() ? 100 : Math.min(100, steps[i].getProgress());
                                out.write("\r\n        <table border=\"0\" width=\"100%\" cellpadding=\"0\" cellspacing=\"1\" class=\"progressBar\">\r\n          <tr>\r\n            ");
                                final Random random = percentage < 0 ? new Random() : null;
                                for (int j = 1; j < 11; j++) {
                                    final boolean done = random != null ? random.nextBoolean() : j * 10 <= percentage;
                                    out.write("\r\n              <td class='");
                                    out.print(done ? "done" : "undone");
                                    out.write("'>&nbsp;</td>\r\n            ");
                                }
                                out.write("\r\n          </tr>\r\n        </table>\r\n      </td>\r\n    ");
                                int evalDoAfterBody = _jspx_th_ifw2_005ftr_005f2.doAfterBody();
                                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                            } while (true);
                            if (_jspx_eval_ifw2_005ftr_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                                out = _jspx_page_context.popBody();
                            }
                        }
                        if (_jspx_th_ifw2_005ftr_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                            _jspx_th_ifw2_005ftr_005f2.release();
                            _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005ftr_005f2);
                            return;
                        }
                        _jspx_th_ifw2_005ftr_005f2.release();
                        _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005ftr_005f2);
                        out.write("\r\n  ");
                    }
                    for (int j = 0; j < (pageSize - steps.length) * 2; j++) {
                        out.write("\r\n    <tr>\r\n      <td colspan=\"3\">  \r\n        &nbsp;    \r\n      </td>\r\n    </tr>\r\n  ");
                    }
                    out.write("\r\n  <tr>\r\n    <td valign=\"bottom\" align=\"left\" colspan=\"3\">\r\n      ");
                    net.infordata.ifw2.web.tags.FormTag _jspx_th_ifw2_005fbnd_002dform_005f0 = (new net.infordata.ifw2.web.tags.FormTag());
                    _jsp_instancemanager.newInstance(_jspx_th_ifw2_005fbnd_002dform_005f0);
                    _jspx_th_ifw2_005fbnd_002dform_005f0.setPageContext(_jspx_page_context);
                    _jspx_th_ifw2_005fbnd_002dform_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ifw2_005ftbody_005f0);
                    _jspx_th_ifw2_005fbnd_002dform_005f0.setBind("actions");
                    int _jspx_eval_ifw2_005fbnd_002dform_005f0 = _jspx_th_ifw2_005fbnd_002dform_005f0.doStartTag();
                    if (_jspx_eval_ifw2_005fbnd_002dform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ifw2_005fbnd_002dform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                            out = _jspx_page_context.pushBody();
                            _jspx_th_ifw2_005fbnd_002dform_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                            _jspx_th_ifw2_005fbnd_002dform_005f0.doInitBody();
                        }
                        do {
                            out.write("\r\n        ");
                            net.infordata.ifw2.web.tags.ActionGroupTag _jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0 = (new net.infordata.ifw2.web.tags.ActionGroupTag());
                            _jsp_instancemanager.newInstance(_jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0);
                            _jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0.setPageContext(_jspx_page_context);
                            _jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ifw2_005fbnd_002dform_005f0);
                            _jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0.setId("a");
                            _jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0.setKeepFormStrokes(true);
                            int _jspx_eval_ifw2_005fbnd_002daction_002dgroup_005f0 = _jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0.doStartTag();
                            if (_jspx_eval_ifw2_005fbnd_002daction_002dgroup_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                                if (_jspx_eval_ifw2_005fbnd_002daction_002dgroup_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                                    out = _jspx_page_context.pushBody();
                                    _jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                                    _jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0.doInitBody();
                                }
                                do {
                                    out.write("\r\n          ");
                                    net.infordata.ifw2.web.tags.RefreshActionTag _jspx_th_ifw2_005fbnd_002drefresh_002daction_005f0 = (new net.infordata.ifw2.web.tags.RefreshActionTag());
                                    _jsp_instancemanager.newInstance(_jspx_th_ifw2_005fbnd_002drefresh_002daction_005f0);
                                    _jspx_th_ifw2_005fbnd_002drefresh_002daction_005f0.setPageContext(_jspx_page_context);
                                    _jspx_th_ifw2_005fbnd_002drefresh_002daction_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0);
                                    _jspx_th_ifw2_005fbnd_002drefresh_002daction_005f0.setIf(future != null);
                                    _jspx_th_ifw2_005fbnd_002drefresh_002daction_005f0.setBind("refresh");
                                    _jspx_th_ifw2_005fbnd_002drefresh_002daction_005f0.setSeconds((future != null && future.isDone()) ? 0 : flow.getRefreshRateInSeconds());
                                    int _jspx_eval_ifw2_005fbnd_002drefresh_002daction_005f0 = _jspx_th_ifw2_005fbnd_002drefresh_002daction_005f0.doStartTag();
                                    if (_jspx_th_ifw2_005fbnd_002drefresh_002daction_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                                        _jspx_th_ifw2_005fbnd_002drefresh_002daction_005f0.release();
                                        _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005fbnd_002drefresh_002daction_005f0);
                                        return;
                                    }
                                    _jspx_th_ifw2_005fbnd_002drefresh_002daction_005f0.release();
                                    _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005fbnd_002drefresh_002daction_005f0);
                                    out.write("\r\n        ");
                                    int evalDoAfterBody = _jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0.doAfterBody();
                                    if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                                } while (true);
                                if (_jspx_eval_ifw2_005fbnd_002daction_002dgroup_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                                    out = _jspx_page_context.popBody();
                                }
                            }
                            if (_jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                                _jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0.release();
                                _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0);
                                return;
                            }
                            _jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0.release();
                            _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005fbnd_002daction_002dgroup_005f0);
                            out.write("\r\n      ");
                            int evalDoAfterBody = _jspx_th_ifw2_005fbnd_002dform_005f0.doAfterBody();
                            if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                        } while (true);
                        if (_jspx_eval_ifw2_005fbnd_002dform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                            out = _jspx_page_context.popBody();
                        }
                    }
                    if (_jspx_th_ifw2_005fbnd_002dform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _jspx_th_ifw2_005fbnd_002dform_005f0.release();
                        _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005fbnd_002dform_005f0);
                        return;
                    }
                    _jspx_th_ifw2_005fbnd_002dform_005f0.release();
                    _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005fbnd_002dform_005f0);
                    out.write("\r\n    </td>\r\n  </tr>\r\n  ");
                    int evalDoAfterBody = _jspx_th_ifw2_005ftbody_005f0.doAfterBody();
                    if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                } while (true);
                if (_jspx_eval_ifw2_005ftbody_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.popBody();
                }
            }
            if (_jspx_th_ifw2_005ftbody_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _jspx_th_ifw2_005ftbody_005f0.release();
                _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005ftbody_005f0);
                return;
            }
            _jspx_th_ifw2_005ftbody_005f0.release();
            _jsp_instancemanager.destroyInstance(_jspx_th_ifw2_005ftbody_005f0);
            out.write("  \r\n</table>\r\n");
        } catch (java.lang.Throwable t) {
            if (!(t instanceof javax.servlet.jsp.SkipPageException)) {
                out = _jspx_out;
                if (out != null && out.getBufferSize() != 0) try {
                    out.clearBuffer();
                } catch (java.io.IOException e) {
                }
                if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
            }
        } finally {
            _jspxFactory.releasePageContext(_jspx_page_context);
        }
    }
}
