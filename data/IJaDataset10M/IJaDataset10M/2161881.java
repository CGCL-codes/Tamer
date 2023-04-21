package cn.myapps.core.shortmessage.runtime;

/**
 * 解析服务器端返回字串,并将数据存储到数据库中
 * @author Chris
 *
 */
public interface IMessageParser {

    /**
	 * 解析服务器端返回字串,并将数据存储到数据库中
	 * @param xml 服务器端返回字串
	 * @throws Exception
	 */
    public void parse(String xml) throws Exception;
}
