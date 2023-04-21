package cn.adamkts.admin.administrator.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import cn.adamkts.admin.administrator.service.PermissionService;
import cn.adamkts.core.AbstractHandlerInterceptor;

/**
 * 后台权限管理拦截器
 * @author Xuanqi.li
 *
 */
@Component
public class PermissionHandlerInterceptor extends AbstractHandlerInterceptor {

    @Resource
    private PermissionService permissionService;

    /**
	 * 对拦截的URL进行权限验证
	 */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return permissionService.checkPermission(request, response);
    }
}
