package tk.solaapps.ohtune.pattern;

import java.util.Date;
import tk.solaapps.ohtune.model.Job;

public class JsonJob {

    public Long order_id;

    public Long id;

    public String number;

    public String order_user;

    public String customer_name;

    public String customer_code;

    public String product_name;

    public String product_our_name;

    public String requirement1;

    public String requirement2;

    public Integer total;

    public Integer remaining;

    public Date order_deadline;

    public Date order_c_deadline;

    public Date start_date;

    public Date complete_date;

    public String section;

    public String order_status;

    public String order_remark;

    public boolean isNew;

    public Integer finished;

    public Integer finished_remark;

    public String status;

    public String handled_by;

    public String product_image;

    public String product_drawing;

    public Integer total_rejected;

    public String assigned_to;

    public Integer priority;

    public String image = "";

    public String drawing = "";

    public JsonJob(Job job) {
        id = job.getId();
        order_id = job.getOrders().getId();
        number = job.getOrders().getNumber();
        if (job.getUserac() == null) order_user = ""; else order_user = job.getOrders().getCreator();
        customer_name = job.getOrders().getCustomer_name();
        customer_code = job.getOrders().getCustomer_code();
        product_name = job.getOrders().getProduct_name();
        product_our_name = job.getOrders().getProduct_our_name();
        requirement1 = job.getOrders().getRequirement_1();
        requirement2 = job.getOrders().getRequirement_2();
        total = job.getTotal();
        order_deadline = job.getOrders().getDeadline();
        order_c_deadline = job.getOrders().getC_deadline();
        start_date = job.getStart_date();
        complete_date = job.getComplete_date();
        section = job.getJob_type().getName();
        order_status = job.getOrders().getStatus();
        order_remark = job.getOrders().getRequirement_4();
        finished = job.getFinished();
        status = job.getStatus();
        handled_by = job.getUserac().getName();
        Date now = new Date();
        isNew = job.getStart_date().getYear() == now.getYear() && job.getStart_date().getMonth() == now.getMonth() && job.getStart_date().getDate() == now.getDate();
        product_image = job.getOrders().getProduct_name();
        product_drawing = job.getOrders().getProduct_name();
        remaining = job.getRemaining();
        total_rejected = job.getTotal_rejected();
        assigned_to = job.getAssigned_to() == null ? "" : job.getAssigned_to().getName();
        priority = job.getOrders().getPriority();
    }
}
