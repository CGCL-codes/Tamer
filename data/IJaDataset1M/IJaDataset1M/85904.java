package SMS;

public class SMSDetails {

    public boolean ERROR = false;

    String data;

    String status;

    String number;

    String date;

    public void generate(String message, String modelName) {
        if (message.indexOf("ERROR") != -1 || message.indexOf(':') <= 0) {
            ERROR = false;
            return;
        }
        int startPoint = message.indexOf("\r", message.indexOf(":") + 1) + 2;
        int endPoint = message.length();
        if (modelName.equals("HUAWEI")) {
            startPoint = message.indexOf("\r", message.indexOf(":") + 1) + 2;
            System.out.println((new StringBuilder()).append("STARTPOINT==").append(startPoint).toString());
            endPoint = message.length();
            System.out.println((new StringBuilder()).append("EndPoint=").append(endPoint).toString());
        } else if (modelName.equals("TATA")) {
            startPoint = message.indexOf(",", 40);
            System.out.println((new StringBuilder()).append("STARTPOINT==").append(startPoint).toString());
            endPoint = message.length();
            System.out.println((new StringBuilder()).append("EndPoint=").append(endPoint).toString());
        } else if (modelName.equals("Zteit")) {
            startPoint = message.indexOf("\r\n", 15);
            System.out.println((new StringBuilder()).append("STARTPOINT==").append(startPoint).toString());
            endPoint = message.length();
            System.out.println((new StringBuilder()).append("EndPoint==").append(endPoint).toString());
        }
        if (endPoint - 8 < 25) {
            ERROR = false;
            return;
        }
        if (modelName.equals("HUAWEI")) {
            data = message.substring(startPoint, endPoint - 8);
            System.out.println((new StringBuilder()).append("sms : ").append(data).toString());
            int start = message.indexOf(',');
            date = message.substring(start + 1, message.indexOf(',', start + 1));
            date = date.replace("<", "");
            date = date.replace("/", "-");
            date = date.replace(">", "");
            status = message.substring(message.indexOf(':') + 1, message.indexOf(','));
            System.out.println((new StringBuilder()).append("THIS IS STatus==").append(status).toString());
            status = status.trim();
            System.out.println((new StringBuilder()).append("THIS IS STatus after trim==").append(status).toString());
            status = status.substring(1, status.length() - 1);
            System.out.println((new StringBuilder()).append("THIS IS STatus final==").append(status).toString());
            number = message.substring(message.indexOf(':') + 1, message.indexOf(','));
            System.out.println((new StringBuilder()).append("Number==").append(number).toString());
            number = number.trim();
            System.out.println((new StringBuilder()).append("Number After Trim==").append(number).toString());
        } else if (modelName.equals("TATA")) {
            data = message.substring(startPoint, endPoint - 4);
            System.out.println((new StringBuilder()).append("sms : ").append(data).toString());
            status = data.substring(0, data.indexOf(",", 3));
            for (int count = 1; count != 5; count++) if (data.indexOf(",") >= 0) data = data.substring(data.indexOf(",") + 1, data.length());
            System.out.println((new StringBuilder()).append("THIS IS STatus==").append(status).toString());
            System.out.println((new StringBuilder()).append("DATA==").append(data).toString());
            status = status.trim();
            if (status.length() < 10) {
                System.out.println((new StringBuilder()).append("String=").append(message).toString());
                status = message.substring(message.indexOf(":") + 3, message.indexOf(",") + 11);
                System.out.println((new StringBuilder()).append("THIS IS NEW STATUS==").append(status).toString());
                number = status;
                System.out.println((new StringBuilder()).append("Number After Trim==").append(number).toString());
            } else {
                System.out.println((new StringBuilder()).append("THIS IS STatus after trim==").append(status).toString());
                status = status.substring(1, status.length());
                System.out.println((new StringBuilder()).append("THIS IS STatus final==").append(status).toString());
                number = status.substring(status.length() - 10, status.length());
                System.out.println((new StringBuilder()).append("Number==").append(number).toString());
                number = number.trim();
                System.out.println((new StringBuilder()).append("Number After Trim==").append(number).toString());
            }
            data = message.substring(startPoint + 2, endPoint - 8);
            System.out.println((new StringBuilder()).append("sms : ").append(data).toString());
            status = message.substring(message.indexOf(",") + 2, message.indexOf(",", message.indexOf(",") + 3));
            System.out.println((new StringBuilder()).append("THIS IS STatus==").append(status).toString());
            status = status.trim();
            System.out.println((new StringBuilder()).append("THIS IS STatus after trim==").append(status).toString());
            status = status.replaceAll("\"", "");
            System.out.println((new StringBuilder()).append("THIS IS STatus final==").append(status).toString());
            System.out.println((new StringBuilder()).append("INDEX==").append(status.indexOf("0")).toString());
            if (status.indexOf("0") == 0 && status.length() > 10) {
                status = status.substring(1, status.length());
                System.out.println((new StringBuilder()).append("THIS IS STATUS").append(status).toString());
            }
            if (status.length() == 10) {
                number = status;
            }
            System.out.println("NUMBER IS NOT 10 DIGIT NO");
        }
        ERROR = true;
    }

    public boolean getError() {
        return ERROR;
    }

    public String getData() {
        return data;
    }

    public String getNumber() {
        if (number.indexOf("0") == 0) {
            number = "+91" + number.substring(1);
        }
        return number;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
