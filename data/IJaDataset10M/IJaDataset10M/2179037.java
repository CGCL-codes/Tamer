package easyJ.database.dao.command;

import easyJ.database.dao.Filter;

public interface FilterableCommand extends Command {

    /**
     * 得到一个拥有条件命令的过滤器。
     * 
     * @return Filter
     */
    public Filter getFilter();

    /**
     * 为一个需要条件的命令设置filter，包括SelectCommand,UpdateCommand。
     * 
     * @param filter
     *                将过滤条件付给FilterableCommand
     * @see <a href="SelectCommand.html">SelectCommand</a>
     * @see <a href="UpdateCommand.html">UpdateCommand</a>
     * @return Filter
     */
    public void setFilter(Filter filter);
}
