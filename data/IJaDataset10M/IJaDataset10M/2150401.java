package purej.web.servlet;

import javax.servlet.http.HttpSession;
import purej.logging.Logger;
import purej.logging.LoggerFactory;
import purej.util.MethodInvoker;

/**
 * <b>��Ʈ�ѷ�</b>
 * 
 * <br>
 * ����Ʈ��Ʈ�ѷ��� �� ����ü�̸�, ����ڰ� ó�� �޼ҵ带 �����Ҷ� ���� �ڵ鸵�� �����մϴ�. <br>
 * ����ڴ� �ΰ�,�Է�,���,��,����,ĳ��,����ȭ �޼��� ���� ��ü�� ���Ǿ��� �ٷ� ����� �� �ֽ��ϴ�. <br>
 * 
 * @author SangBoo Lee
 * @since 2007-05-27
 */
public abstract class Controller implements ControllerCommand {

    public static Logger logger;

    protected HTTPModel httpModel;

    protected Input input;

    protected Output output;

    protected Message message;

    protected HttpSession session;

    protected Authentication authentication;

    /**
     * Ŀ�ǵ� ���� �޼ҵ� ����
     */
    public HTTPModel execute(HTTPModel httpModel) throws Exception {
        try {
            this.httpModel = httpModel;
            logger = LoggerFactory.getLogger(httpModel.getControllerMapping().getControllerClass().getName(), Logger.CONTROLLER);
            input = httpModel.getInput();
            output = httpModel.getOutput();
            session = httpModel.getSession();
            message = httpModel.getMessage();
            authentication = httpModel.getAuthentication();
            try {
                MethodInvoker mk = new MethodInvoker();
                mk.setTargetObject(httpModel.getControllerMapping().getControllerClass().newInstance());
                mk.setTargetMethod(httpModel.getControllerMapping().getProcessMethod());
                mk.prepare();
                View view = (View) mk.invoke();
                httpModel.setView(view);
            } catch (NoSuchMethodException nse) {
                logger.error("Not found process method : [" + httpModel.getControllerMapping().getProcessMethod() + "], dispath view [NOT_FOUND_COMMAND]");
            } catch (Exception ne) {
                throw new ControllerException("Error process method from controller class : [" + httpModel.getControllerMapping().getProcessMethod() + "] : " + ne.getCause().getMessage(), ne.getCause());
            }
        } catch (Exception ex) {
            throw ex;
        }
        return httpModel;
    }
}
