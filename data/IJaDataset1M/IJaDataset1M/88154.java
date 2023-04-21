package org.wdcode.base.bean;

import org.wdcode.base.params.WdBaseParams;

/**
 * 分页信息保存的实体Bean 在分页Dao和Tag之间传递值用
 * @author WD
 * @since JDK6
 * @version 1.0 2009-04-21
 */
public final class PageBean {

    private int totalSize;

    private int currentPage = 1;

    private int pageSize = WdBaseParams.getPageSize();

    /**
	 * 获得总页数
	 * @return 总页数
	 */
    public final int getTotalPage() {
        return totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1;
    }

    /**
	 * 获得每页显示数量
	 * @return 每页显示数量
	 */
    public final int getPageSize() {
        return pageSize;
    }

    /**
	 * 设置每页显示数量
	 * @param pageSize 每页显示数量
	 */
    public final void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
	 * 获得总数量
	 * @return 总数量
	 */
    public final int getTotalSize() {
        return totalSize;
    }

    /**
	 * 设置总数量
	 * @param totalSize 总数量
	 */
    public final void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    /**
	 * 获得当前显示页
	 * @return 当前显示页
	 */
    public final int getCurrentPage() {
        return currentPage;
    }

    /**
	 * 设置当前显示页
	 * @param currentPage 当前显示页
	 */
    public final void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
	 * 获得开始页码
	 * @return 开始页码
	 */
    public final int getStartPage() {
        return currentPage - 5 > 0 ? currentPage - 5 : 1;
    }

    /**
	 * 获得结束页码
	 * @return 结束页码
	 */
    public final int getEndPage() {
        int current = getCurrentPage();
        int total = getTotalPage();
        return (current == 1 || current < 6) ? (total > 10 ? 10 : total) : (current + 5 <= total ? current + 5 : total);
    }
}
