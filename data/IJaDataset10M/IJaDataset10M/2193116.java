package lichen.services;

/**
 * 系统所需要的一写编码.
 * @author jcai
 * @version $Revision: 191 $
 * @since 0.0.3
 */
public interface Encoder {

    /**
	 * 对用户的密码进行编码.
	 * @param plainPassword 明文密码.
	 * @return 加密后的密码.
	 * @since 0.0.3
	 */
    public String encodPassword(String plainPassword);

    /**
	 * 对url进行编码
	 * @param planUrl 明文的url.
	 * @return 加密后的url.
	 * @since 0.0.3
	 */
    public String encodUrl(String planUrl);

    /**
	 * 对url进行解密.
	 * @param securedUrl 加密后的url.
	 * @return 明文url.
	 * @since 0.0.3
	 */
    public String decodUrl(String securedUrl);
}
