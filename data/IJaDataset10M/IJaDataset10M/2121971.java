package jp.co.baka.gae.lib;

import java.util.logging.Logger;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public abstract class MyController extends Controller {

    private static final Logger logger = Logger.getLogger(MyController.class.getName());

    protected Navigation baseRelativeForward(String path) {
        return forward(basePath + path);
    }

    protected Navigation baseRelativeRedirect(String path) {
        return redirect(basePath + path);
    }

    /**
     * runで例外発生時処理
     *
     */
    @Override
    public Navigation handleError(Throwable error) {
        return forward("/bbs/index.jsp");
    }
}
