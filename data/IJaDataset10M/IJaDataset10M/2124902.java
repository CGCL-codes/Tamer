package system.controller;

import com.sysnet_pioneer.controller.Controller;

public class publicController extends Controller {

    public void index() {
        super.renderView("index");
    }

    public void aboutUs() {
        super.renderView("aboutUs");
    }

    public void services() {
        super.renderView("services");
    }

    public void brgy_clearance() {
        super.renderView("brgy_clearance");
    }

    public void register_resident() {
        super.renderView("register_resident");
    }

    public void renew_franchise() {
        super.renderView("renewal_franchise");
    }

    public void trisikad_caught() {
        super.renderView("trisikad_caught");
    }

    public void business_permit() {
        super.renderView("business_permit");
    }

    public void b_d_o_e_r_n() {
        super.renderView("b_d_o_e_r_n");
    }

    public void veco_electricity_installation() {
        super.renderView("veco_electricity_installation");
    }

    public void sss_application() {
        super.renderView("sss_application");
    }

    public void resident_dir() {
        super.renderView("resident_directory");
    }

    public void print_id() {
        super.renderView("print_id");
    }
}
