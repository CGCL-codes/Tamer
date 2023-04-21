package jsystem.runner.agent.publisher;

import java.util.Map;
import jsystem.framework.report.ListenerstManager;
import jsystem.framework.report.Reporter;
import jsystem.runner.remote.Message;
import jsystem.runner.remote.RemoteTestRunner.RemoteMessage;

public class EmptyPublisher implements Publisher {

    private Reporter report = ListenerstManager.getInstance();

    @Override
    public void publish() {
        report.report("No publisher was defined", 2);
    }

    @Override
    public Message publish(Map<String, Object> publishProperties) throws Exception {
        report.report("No publisher was defined", 2);
        Message message = new Message();
        message.setType(RemoteMessage.M_PUBLISH);
        return message;
    }

    @Override
    public boolean validatePublisher(Object object, String... dbSettingParams) {
        return false;
    }
}
