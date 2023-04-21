package bizcal.web;

public interface WebCalendarCallback {

    public String getDetailURL() throws Exception;

    public String getStarttimeParamName() throws Exception;

    public String getCalendarParamName() throws Exception;

    public class BaseImpl implements WebCalendarCallback {

        @Override
        public String getDetailURL() throws Exception {
            return null;
        }

        @Override
        public String getStarttimeParamName() throws Exception {
            return "starttime";
        }

        @Override
        public String getCalendarParamName() throws Exception {
            return "cal";
        }
    }
}
