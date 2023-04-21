package com.k42b3.neodym.oauth;

/**
 * PLAINTEXT
 *
 * @author     Christoph Kappestein <k42b3.x@gmail.com>
 * @license    http://www.gnu.org/licenses/gpl.html GPLv3
 * @link       http://code.google.com/p/delta-quadrant
 * @version    $Revision: 205 $
 */
public class PLAINTEXT implements SignatureInterface {

    public String build(String baseString, String consumerSecret, String tokenSecret) {
        String key = Oauth.urlEncode(consumerSecret) + "&" + Oauth.urlEncode(tokenSecret);
        return key;
    }
}
