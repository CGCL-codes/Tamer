package reportoperations;

import timer.ExecutionTimer;
import timer.TimerRecordFile;
import dbmanager.DBManager;

public class Srno implements Operation {

    String buffer[];

    DBManager database;

    String object;

    String heading;

    public Srno() {
    }

    public String[] getbuffer() {
        return buffer;
    }

    public void getresult() {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        StringBuffer strbuffer = new StringBuffer("Srno" + "\t" + buffer[0] + "\n");
        for (int i = 1; i < buffer.length; i++) strbuffer.append(i + "\t" + buffer[i] + "\n");
        buffer = strbuffer.toString().split("\n");
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("reportoperations", "Srno", "getresult", t.duration());
    }

    public void setDatabase(DBManager database) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        this.database = database;
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("reportoperations", "Srno", "setDatabase", t.duration());
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setbuffer(String[] buffer) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        this.buffer = buffer;
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("reportoperations", "Srno", "setbuffer", t.duration());
    }

    public static void main(String[] args) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        Srno SRNO = new Srno();
        String buffer[] = { "Id	Order_date	Delivery_date	Transaction_Type	Seller	Buyer	Amount	Vat	SalesTax	ServiceTax	Octroi	Total_Amount", "9	2007-05-31 17:23:22	2007-05-31 17:05:21	PO	Open Source Pool	Sourave General Store	705.00	null	null	null	null	705.00", "9	2007-05-31 17:23:22	2007-05-31 17:05:21	PO	Open Source Pool	Sourave General Store	705.00	null	null	null	null	705.00" };
        SRNO.setbuffer(buffer);
        SRNO.getresult();
        buffer = SRNO.getbuffer();
        for (int i = 0; i < buffer.length; i++) System.out.println(buffer[i]);
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("reportoperations", "Srno", "main", t.duration());
    }

    public void setindexofField(int field) {
    }

    public String getfield() {
        return "Srno";
    }

    public void setHeading(String heading) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        this.heading = heading;
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("reportoperations", "Srno", "setHeading", t.duration());
    }
}
