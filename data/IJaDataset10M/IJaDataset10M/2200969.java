package tw.qing.lwdba;

public class DefaultJdbcInformationProvider extends JDBCInfoProvider {

    public DefaultJdbcInformationProvider(JDBCInfo info) {
        super(info);
    }

    public String getPassword() {
        return info.getPassword();
    }

    public String getUserName() {
        return info.getUserName();
    }
}
