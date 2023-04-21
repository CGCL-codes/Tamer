package iwallet.common.request;

/**
 * @author 黄源河
 *
 */
public class GetAccountFrameRequest implements UnrevertableRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String username;

    public GetAccountFrameRequest(String username) {
        this.username = username;
    }

    @Override
    public String getRequestName() {
        return "获得账号数据框架";
    }
}
