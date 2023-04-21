package slim3.controller.attend.manage.attendance.member;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class DispController extends Controller {

    @Override
    public Navigation run() throws Exception {
        return forward("disp.jsp");
    }
}
