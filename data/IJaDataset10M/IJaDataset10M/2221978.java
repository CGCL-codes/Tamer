package cactus.tools.oauth.v1_0;

import java.util.List;

public class RequestData {

    private String httpMethod;

    private String callback_url;

    private List<Parameter> customParameters;

    private String timestamp;

    private String nonce;
}
