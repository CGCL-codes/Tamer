package wsl.fw.msgserver;

public class MsExContactItr extends MsExMessageItr {

    public MsExContactItr(String loginUrl) throws MessageServerException {
        super(loginUrl);
        MSExchangeInterface.createContactMsgIterator(getLoginUrl(), getId());
    }
}
