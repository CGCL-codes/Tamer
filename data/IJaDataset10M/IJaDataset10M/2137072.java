package member;

public abstract class Status {

    abstract String returnStatus();
}

class StatusRegular extends Status {

    public String returnStatus() {
        return "Regular";
    }
}

class StatusElite extends Status {

    public String returnStatus() {
        return "Elite";
    }
}
