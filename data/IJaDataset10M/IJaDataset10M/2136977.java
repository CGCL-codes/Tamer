package coyousoft.jiuhuabook.config;

import java.util.ListResourceBundle;

public class Messages extends ListResourceBundle {

    private static final Object[][] contents = { { "user.userId", "用户ID" }, { "user.userName", "用户名" }, { "user.userPassword", "用户密码" }, { "user.confirmPassword", "确认密码" }, { "user.userRealName", "真实姓名" }, { "user.userEmail", "电子邮箱" }, { "user.userCdate", "创建日期" }, { "user.userUdate", "修改日期" }, { "user.userPassword.inconsistent", "对不起，您两次输入的密码不一致，请重新输入。" }, { "user.userPassword.error", "对不起，您输入的密码 {0} 错误，密码由6~8个英文字符、数字、下划线组成。" }, { "user.userName.error", "对不起，您输入的用户名 {0} 错误，用户名由4~20个英文字符、数字、下划线组成。" }, { "user.userName.invalid", "对不起，您输入的用户名 {0} 已经被其他用户注册，请重新输入。" }, { "user.userEmail.invalid", "对不起，您输入的电子邮箱 {0} 已经被其他用户注册，请重新输入。" }, { "jsp.index.title", "javaeedemo-首页" }, { "jsp.index.searchModule", "搜索模块" }, { "jsp.index.newAdd", "新增用户" }, { "jsp.addUser.title", "javaeedemo-新增用户" }, { "jsp.addUser.success", "已成功新增用户 {0}，您可以继续新增用户，或返回首页继续浏览。" }, { "jsp.addUser.confirm", "确认提交" }, { "jsp.editUser.title", "javaeedemo-编辑用户" }, { "jsp.editUser.success", "已成功修改用户信息，您可以继续修改，或返回首页继续浏览。" }, { "generic.back_to_index", "返回首页" }, { "generic.search", "查询" }, { "generic.delete", "删除" }, { "generic.edit", "编辑" }, { "generic.search.nodata", "很抱歉，没有符合条件的数据。" }, { "generic.pagination.title", "每页显示{0}条记录 当前第{1}页/共计{2}页" }, { "generic.pagination.prev", "上一页" }, { "generic.pagination.next", "下一页" }, { "", "" }, { "", "" }, { "", "" }, { "", "" }, { "", "" }, { "", "" }, { "", "" }, { "", "" }, { "", "" }, { "", "" } };

    @Override
    public Object[][] getContents() {
        return contents;
    }
}
