package magoffin.matt.ieat.web;

import javax.servlet.http.HttpServletRequest;
import magoffin.matt.ieat.biz.BizContext;
import magoffin.matt.ieat.biz.UserBiz;
import magoffin.matt.ieat.domain.UiEdit;
import magoffin.matt.ieat.domain.UiSearchResults;
import magoffin.matt.ieat.domain.User;
import magoffin.matt.ieat.util.UserCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

/**
 * Form controller for editing an existing user.
 * 
 * @author Matt Magoffin (spamsqr@msqr.us)
 * @version $Revision: 4 $ $Date: 2009-04-30 23:27:53 -0400 (Thu, 30 Apr 2009) $
 */
public class EditUserForm extends AddUserForm {

    /**
	 * Get a UserCommand instance.
	 * 
	 * @param request the request
	 * @return the UserCommand
	 * @throws Exception if an error occurs
	 */
    protected UserCommand getEditUserCommand(HttpServletRequest request) throws Exception {
        UserCommand uc = new UserCommand();
        ServletRequestDataBinder binder = createBinder(request, uc);
        binder.bind(request);
        return uc;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        BizContext context = getBizContext(request);
        UserCommand uc = getEditUserCommand(request);
        User user = userBiz.getUserById(uc.getUserId(), context);
        User copyUser = getDomainObjectFactory().getUserInstance();
        BeanUtils.copyProperties(user, copyUser);
        copyUser.setPassword(UserBiz.DO_NOT_CHANGE_VALUE);
        UiEdit edit = getDomainObjectFactory().getUiEditInstance();
        if (uc.getIndex() != null) {
            UiSearchResults sr = getDomainObjectFactory().getEmbeddedSearchResultsInstance();
            WebUtil.initSearchResults(sr, uc.getIndex(), null, getDomainObjectFactory());
            edit.setSearchResults(sr);
        }
        edit.setUser(copyUser);
        return edit;
    }

    @Override
    protected BizContext getBizContext(HttpServletRequest request) {
        return WebUtil.getBizContext(request, true);
    }
}
