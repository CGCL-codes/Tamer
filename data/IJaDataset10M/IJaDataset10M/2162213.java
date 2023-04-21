package org.open4j.cache;

/***
 * 
 * @author fanyifeng
 * 会话接口， 保存用户的数据
 *
 */
public interface IOpenSession {

    public String getId();

    public void setId(String id);

    /**
	 * 最后访问时间
	 */
    public long getLastAccessTime();

    /**
	 * 设置最后访问时间为当前时间
	 */
    public void recordAccessTime(long now);
}
