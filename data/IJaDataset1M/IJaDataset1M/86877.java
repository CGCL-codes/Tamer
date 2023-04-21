package fb.rt.visual.model;

import fb.datatype.*;
import fb.rt.*;
import fb.rt.events.*;

/** FUNCTION_BLOCK Pallet_constrained_model
  * @author JHC
  * @version 20070803/JHC  */
public class Pallet_constrained_model extends FBInstance {

    public BOOL dock_pallet_present = new BOOL();

    public UINT init_pallet_x = new UINT();

    public UINT init_pallet_y = new UINT();

    public UINT x_pincer_left = new UINT();

    public UINT y_pincer_left = new UINT();

    public UINT speed_pallet_fall = new UINT();

    public UINT Collector1_X = new UINT();

    public UINT Collector1_Y = new UINT();

    public UINT Collector2_X = new UINT();

    public UINT Collector2_Y = new UINT();

    public BOOL pincer_closed = new BOOL();

    public BOOL pincer_open = new BOOL();

    public UINT init_docker_x = new UINT();

    public UINT init_docker_y = new UINT();

    public UINT TV_collector1_Y = new UINT();

    public UINT TV_collector2_Y = new UINT();

    public UINT TV_slide_speed = new UINT();

    public UINT TV_pallet1_y = new UINT();

    public UINT out_pallet_x = new UINT();

    public UINT out_pallet_y = new UINT();

    public UINT out_pallet_x1 = new UINT();

    public UINT out_pallet_y1 = new UINT();

    public BOOL disp_pallet = new BOOL();

    public UINT Collector1_X_out = new UINT();

    public UINT Collector1_Y_out = new UINT();

    public UINT Collector2_X_out = new UINT();

    public UINT Collector2_Y_out = new UINT();

    public UINT init_docker_x_out = new UINT();

    public UINT init_docker_y_out = new UINT();

    public UINT TV_out_pallet_z = new UINT();

    public UINT temp_Y_val = new UINT();

    public UINT temp_X_val = new UINT();

    public UINT fall_speed = new UINT();

    public UINT temp_collector1_X_val = new UINT();

    public UINT temp_collector1_Y_val = new UINT();

    public UINT temp_collector2_X_val = new UINT();

    public UINT temp_collector2_Y_val = new UINT();

    public UINT x_offset_docker = new UINT();

    public UINT y_offset_docker = new UINT();

    public UINT y_offset_collector = new UINT();

    public UINT x_min_offset_collector = new UINT();

    public UINT x_max_offset_collector = new UINT();

    public UINT y_inner_height_pincers = new UINT();

    public UINT x_inner_width_pincers = new UINT();

    public BOOL picked_up = new BOOL();

    public BOOL docker_range = new BOOL();

    public UINT temp_docker_x = new UINT();

    public UINT temp_docker_y = new UINT();

    public UINT width_docker_x = new UINT();

    public UINT width_pallet_x = new UINT();

    public UINT height_pallet_y = new UINT();

    public UINT width_collector_x = new UINT();

    public UINT height_pincers_y = new UINT();

    public UINT temp_offset = new UINT();

    public UINT z_offset_collector1 = new UINT();

    public UINT z_pallet_mv = new UINT();

    public UINT slide_speed = new UINT();

    public UINT z_collector_y_temp = new UINT();

    public UINT z_collector2_y_temp = new UINT();

    public EventServer INIT = new EventInput(this);

    public EventServer CLK = new EventInput(this);

    public EventOutput INITO = new EventOutput();

    public EventOutput CLK_out = new EventOutput();

    public EventServer eiNamed(String s) {
        if (s.equals("INIT")) return INIT;
        if (s.equals("CLK")) return CLK;
        return super.eiNamed(s);
    }

    public EventOutput eoNamed(String s) {
        if (s.equals("INITO")) return INITO;
        if (s.equals("CLK_out")) return CLK_out;
        return super.eoNamed(s);
    }

    public ANY ivNamed(String s) throws FBRManagementException {
        if (s.equals("dock_pallet_present")) return dock_pallet_present;
        if (s.equals("init_pallet_x")) return init_pallet_x;
        if (s.equals("init_pallet_y")) return init_pallet_y;
        if (s.equals("x_pincer_left")) return x_pincer_left;
        if (s.equals("y_pincer_left")) return y_pincer_left;
        if (s.equals("speed_pallet_fall")) return speed_pallet_fall;
        if (s.equals("Collector1_X")) return Collector1_X;
        if (s.equals("Collector1_Y")) return Collector1_Y;
        if (s.equals("Collector2_X")) return Collector2_X;
        if (s.equals("Collector2_Y")) return Collector2_Y;
        if (s.equals("pincer_closed")) return pincer_closed;
        if (s.equals("pincer_open")) return pincer_open;
        if (s.equals("init_docker_x")) return init_docker_x;
        if (s.equals("init_docker_y")) return init_docker_y;
        if (s.equals("TV_collector1_Y")) return TV_collector1_Y;
        if (s.equals("TV_collector2_Y")) return TV_collector2_Y;
        if (s.equals("TV_slide_speed")) return TV_slide_speed;
        if (s.equals("TV_pallet1_y")) return TV_pallet1_y;
        return super.ivNamed(s);
    }

    public ANY ovNamed(String s) throws FBRManagementException {
        if (s.equals("out_pallet_x")) return out_pallet_x;
        if (s.equals("out_pallet_y")) return out_pallet_y;
        if (s.equals("out_pallet_x1")) return out_pallet_x1;
        if (s.equals("out_pallet_y1")) return out_pallet_y1;
        if (s.equals("disp_pallet")) return disp_pallet;
        if (s.equals("Collector1_X_out")) return Collector1_X_out;
        if (s.equals("Collector1_Y_out")) return Collector1_Y_out;
        if (s.equals("Collector2_X_out")) return Collector2_X_out;
        if (s.equals("Collector2_Y_out")) return Collector2_Y_out;
        if (s.equals("init_docker_x_out")) return init_docker_x_out;
        if (s.equals("init_docker_y_out")) return init_docker_y_out;
        if (s.equals("TV_out_pallet_z")) return TV_out_pallet_z;
        return super.ovNamed(s);
    }

    public void connectIV(String ivName, ANY newIV) throws FBRManagementException {
        if (ivName.equals("dock_pallet_present")) {
            connect_dock_pallet_present((BOOL) newIV);
            return;
        }
        if (ivName.equals("init_pallet_x")) {
            connect_init_pallet_x((UINT) newIV);
            return;
        }
        if (ivName.equals("init_pallet_y")) {
            connect_init_pallet_y((UINT) newIV);
            return;
        }
        if (ivName.equals("x_pincer_left")) {
            connect_x_pincer_left((UINT) newIV);
            return;
        }
        if (ivName.equals("y_pincer_left")) {
            connect_y_pincer_left((UINT) newIV);
            return;
        }
        if (ivName.equals("speed_pallet_fall")) {
            connect_speed_pallet_fall((UINT) newIV);
            return;
        }
        if (ivName.equals("Collector1_X")) {
            connect_Collector1_X((UINT) newIV);
            return;
        }
        if (ivName.equals("Collector1_Y")) {
            connect_Collector1_Y((UINT) newIV);
            return;
        }
        if (ivName.equals("Collector2_X")) {
            connect_Collector2_X((UINT) newIV);
            return;
        }
        if (ivName.equals("Collector2_Y")) {
            connect_Collector2_Y((UINT) newIV);
            return;
        }
        if (ivName.equals("pincer_closed")) {
            connect_pincer_closed((BOOL) newIV);
            return;
        }
        if (ivName.equals("pincer_open")) {
            connect_pincer_open((BOOL) newIV);
            return;
        }
        if (ivName.equals("init_docker_x")) {
            connect_init_docker_x((UINT) newIV);
            return;
        }
        if (ivName.equals("init_docker_y")) {
            connect_init_docker_y((UINT) newIV);
            return;
        }
        if (ivName.equals("TV_collector1_Y")) {
            connect_TV_collector1_Y((UINT) newIV);
            return;
        }
        if (ivName.equals("TV_collector2_Y")) {
            connect_TV_collector2_Y((UINT) newIV);
            return;
        }
        if (ivName.equals("TV_slide_speed")) {
            connect_TV_slide_speed((UINT) newIV);
            return;
        }
        if (ivName.equals("TV_pallet1_y")) {
            connect_TV_pallet1_y((UINT) newIV);
            return;
        }
        super.connectIV(ivName, newIV);
    }

    public void connect_dock_pallet_present(BOOL newIV) throws FBRManagementException {
        dock_pallet_present = newIV;
    }

    public void connect_init_pallet_x(UINT newIV) throws FBRManagementException {
        init_pallet_x = newIV;
    }

    public void connect_init_pallet_y(UINT newIV) throws FBRManagementException {
        init_pallet_y = newIV;
    }

    public void connect_x_pincer_left(UINT newIV) throws FBRManagementException {
        x_pincer_left = newIV;
    }

    public void connect_y_pincer_left(UINT newIV) throws FBRManagementException {
        y_pincer_left = newIV;
    }

    public void connect_speed_pallet_fall(UINT newIV) throws FBRManagementException {
        speed_pallet_fall = newIV;
    }

    public void connect_Collector1_X(UINT newIV) throws FBRManagementException {
        Collector1_X = newIV;
    }

    public void connect_Collector1_Y(UINT newIV) throws FBRManagementException {
        Collector1_Y = newIV;
    }

    public void connect_Collector2_X(UINT newIV) throws FBRManagementException {
        Collector2_X = newIV;
    }

    public void connect_Collector2_Y(UINT newIV) throws FBRManagementException {
        Collector2_Y = newIV;
    }

    public void connect_pincer_closed(BOOL newIV) throws FBRManagementException {
        pincer_closed = newIV;
    }

    public void connect_pincer_open(BOOL newIV) throws FBRManagementException {
        pincer_open = newIV;
    }

    public void connect_init_docker_x(UINT newIV) throws FBRManagementException {
        init_docker_x = newIV;
    }

    public void connect_init_docker_y(UINT newIV) throws FBRManagementException {
        init_docker_y = newIV;
    }

    public void connect_TV_collector1_Y(UINT newIV) throws FBRManagementException {
        TV_collector1_Y = newIV;
    }

    public void connect_TV_collector2_Y(UINT newIV) throws FBRManagementException {
        TV_collector2_Y = newIV;
    }

    public void connect_TV_slide_speed(UINT newIV) throws FBRManagementException {
        TV_slide_speed = newIV;
    }

    public void connect_TV_pallet1_y(UINT newIV) throws FBRManagementException {
        TV_pallet1_y = newIV;
    }

    private static final int index_START = 0;

    private void state_START() {
        eccState = index_START;
    }

    private static final int index_INIT = 1;

    private void state_INIT() {
        eccState = index_INIT;
        alg_INIT();
        INITO.serviceEvent(this);
        state_START();
    }

    private static final int index_REQ = 2;

    private void state_REQ() {
        eccState = index_REQ;
        alg_REQ();
        CLK_out.serviceEvent(this);
        state_START();
    }

    public Pallet_constrained_model() {
        super();
    }

    public void serviceEvent(EventServer e) {
        if (e == INIT) service_INIT(); else if (e == CLK) service_CLK();
    }

    public void service_INIT() {
        if ((eccState == index_START)) state_INIT();
    }

    public void service_CLK() {
        if ((eccState == index_START)) state_REQ();
    }

    /** ALGORITHM INIT IN Java*/
    public void alg_INIT() {
        fall_speed.value = speed_pallet_fall.value;
        temp_collector1_X_val.value = Collector1_X.value;
        temp_collector1_Y_val.value = Collector1_Y.value;
        temp_collector2_X_val.value = Collector2_X.value;
        temp_collector2_Y_val.value = Collector2_Y.value;
        temp_docker_y.value = init_docker_y.value;
        temp_docker_x.value = init_docker_x.value;
        temp_X_val.value = init_pallet_x.value;
        temp_Y_val.value = init_pallet_y.value;
        picked_up.value = false;
        Collector1_X_out.value = Collector1_X.value;
        Collector1_Y_out.value = Collector1_Y.value;
        Collector2_X_out.value = Collector2_X.value;
        Collector2_Y_out.value = Collector2_Y.value;
        init_docker_x_out.value = init_docker_x.value;
        init_docker_y_out.value = init_docker_y.value;
        z_collector_y_temp.value = TV_collector1_Y.value;
        z_collector2_y_temp.value = TV_collector2_Y.value;
        z_pallet_mv.value = TV_pallet1_y.value;
        slide_speed.value = TV_slide_speed.value;
        if (dock_pallet_present.value == true) {
            out_pallet_x.value = temp_X_val.value;
            out_pallet_y.value = temp_Y_val.value;
            out_pallet_x1.value = temp_X_val.value;
            out_pallet_y1.value = temp_Y_val.value;
            TV_out_pallet_z.value = z_pallet_mv.value;
            disp_pallet.value = true;
        } else {
            disp_pallet.value = false;
        }
    }

    /** ALGORITHM REQ IN Java*/
    public void alg_REQ() {
        width_docker_x.value = 40;
        width_collector_x.value = 57;
        width_pallet_x.value = 28;
        height_pallet_y.value = 18;
        height_pincers_y.value = 26;
        x_offset_docker.value = 6;
        y_offset_docker.value = 6;
        x_min_offset_collector.value = 9;
        x_max_offset_collector.value = 48;
        y_offset_collector.value = 14;
        x_inner_width_pincers.value = 5;
        y_inner_height_pincers.value = 15;
        z_offset_collector1.value = 5;
        if (dock_pallet_present.value == true) {
            System.out.println(" ******** PALLET 1 Present  *********");
            disp_pallet.value = true;
            if (((x_pincer_left.value + x_inner_width_pincers.value) == temp_X_val.value) && picked_up.value == false) {
                if (((y_pincer_left.value + y_inner_height_pincers.value) <= temp_Y_val.value) && (y_pincer_left.value + height_pincers_y.value >= temp_Y_val.value)) {
                    picked_up.value = true;
                    temp_offset.value = temp_Y_val.value - (y_pincer_left.value + y_inner_height_pincers.value);
                    System.out.println(" picked up\n");
                    System.out.println("\n" + temp_offset.value + "\n");
                }
            }
            System.out.println(" VLAUE of PALLET X  -- " + temp_X_val.value + " \n");
            System.out.println(" VLAUE of PALLET Y  -- " + temp_Y_val.value + " \n");
            System.out.println(" VLAUE of COllector X  -- " + temp_collector1_X_val.value + " \n");
            System.out.println(" VLAUE of COllector Y  -- " + temp_collector1_Y_val.value + " \n");
            if ((picked_up.value == true) && (pincer_closed.value == true)) {
                temp_X_val.value = x_pincer_left.value + x_inner_width_pincers.value;
                temp_Y_val.value = y_pincer_left.value + y_inner_height_pincers.value + temp_offset.value;
            } else if (pincer_open.value == true) {
                picked_up.value = false;
            }
            if (picked_up.value == false) {
                if (((temp_X_val.value >= temp_docker_x.value) && ((temp_X_val.value + width_pallet_x.value) <= (temp_docker_x.value + width_docker_x.value))) || ((temp_X_val.value < temp_docker_x.value) && ((temp_X_val.value + width_pallet_x.value) > temp_docker_x.value))) {
                    System.out.println(" docker min good \n");
                    System.out.println(" docker max good  ----> into docker \n");
                    docker_range.value = false;
                    if ((temp_docker_y.value + y_offset_docker.value) - (temp_Y_val.value + height_pallet_y.value) < 21) {
                        temp_X_val.value = temp_X_val.value;
                        temp_Y_val.value = temp_docker_y.value + y_offset_docker.value - height_pallet_y.value;
                        docker_range.value = true;
                        System.out.println(" TRUE 1 \n");
                    }
                } else if ((temp_X_val.value <= (temp_docker_x.value + width_docker_x.value)) && ((temp_X_val.value + width_pallet_x.value) >= (temp_docker_x.value + width_docker_x.value))) {
                    System.out.println(" docker 0. 5 min good \n");
                    System.out.println(" docker max good  ----> into docker \n");
                    docker_range.value = false;
                    if ((temp_docker_y.value + y_offset_docker.value) - (temp_Y_val.value + height_pallet_y.value) < 21) {
                        temp_X_val.value = temp_X_val.value;
                        temp_Y_val.value = temp_docker_y.value - height_pallet_y.value;
                        docker_range.value = true;
                        System.out.println(" TRUE 1 \n");
                    }
                } else if (((temp_X_val.value >= (temp_collector1_X_val.value + x_min_offset_collector.value)) && ((temp_X_val.value + width_pallet_x.value) <= (temp_collector1_X_val.value + x_max_offset_collector.value)))) {
                    System.out.println(" collector1 min good \n");
                    System.out.println(" collector1 max good  ----> into collector 1 \n");
                    docker_range.value = false;
                    if ((temp_collector1_Y_val.value + y_offset_collector.value) - (temp_Y_val.value + height_pallet_y.value) < 21) {
                        temp_X_val.value = temp_X_val.value;
                        temp_Y_val.value = temp_collector1_Y_val.value + y_offset_collector.value - height_pallet_y.value;
                        docker_range.value = true;
                        System.out.println(" TRUE 1 \n");
                        System.out.println("\n z_collector_y_temp = " + z_collector_y_temp.value + "\n");
                        System.out.println("\n z_offset_collector1 = " + z_offset_collector1.value + "\n");
                        System.out.println("\n " + (z_pallet_mv.value - slide_speed.value) + " > " + ((z_collector_y_temp.value + z_offset_collector1.value)) + "\n");
                        if ((z_pallet_mv.value - slide_speed.value) > (z_collector_y_temp.value + z_offset_collector1.value)) {
                            z_pallet_mv.value = z_pallet_mv.value - slide_speed.value;
                        }
                        System.out.println("\n z = " + z_pallet_mv.value + "\n");
                    }
                } else if (((temp_X_val.value < (temp_collector1_X_val.value + width_collector_x.value)) && ((temp_X_val.value + width_pallet_x.value) > (temp_collector1_X_val.value + x_max_offset_collector.value))) || ((temp_X_val.value < (temp_collector1_X_val.value + x_min_offset_collector.value) && ((temp_X_val.value + width_pallet_x.value) > (temp_collector1_X_val.value))))) {
                    System.out.println(" collector1 0. 5 min good \n");
                    System.out.println(" collector1 max good  ----> into collector1 \n");
                    docker_range.value = false;
                    if ((temp_collector1_Y_val.value) - (temp_Y_val.value + height_pallet_y.value) < 21) {
                        temp_X_val.value = temp_X_val.value;
                        temp_Y_val.value = temp_collector1_Y_val.value - height_pallet_y.value;
                        docker_range.value = true;
                        System.out.println(" TRUE 1 \n");
                    }
                } else if (((temp_X_val.value >= (temp_collector2_X_val.value + x_min_offset_collector.value)) && ((temp_X_val.value + width_pallet_x.value) <= (temp_collector2_X_val.value + x_max_offset_collector.value)))) {
                    System.out.println(" collector2 min good \n");
                    System.out.println(" collector2 max good  ----> into collector 1 \n");
                    docker_range.value = false;
                    if ((temp_collector2_Y_val.value + y_offset_collector.value) - (temp_Y_val.value + height_pallet_y.value) < 21) {
                        temp_X_val.value = temp_X_val.value;
                        temp_Y_val.value = temp_collector2_Y_val.value + y_offset_collector.value - height_pallet_y.value;
                        docker_range.value = true;
                        System.out.println(" TRUE 1 \n");
                        System.out.println("\n z_collector2_y_temp = " + z_collector2_y_temp.value + "\n");
                        System.out.println("\n z_offset_collector1 = " + z_offset_collector1.value + "\n");
                        System.out.println("\n " + (z_pallet_mv.value - slide_speed.value) + " > " + ((z_collector2_y_temp.value + z_offset_collector1.value)) + "\n");
                        if ((z_pallet_mv.value - slide_speed.value) > (z_collector2_y_temp.value + z_offset_collector1.value)) {
                            z_pallet_mv.value = z_pallet_mv.value - slide_speed.value;
                        }
                        System.out.println("\n z = " + z_pallet_mv.value + "\n");
                    }
                } else if (((temp_X_val.value < (temp_collector2_X_val.value + width_collector_x.value)) && ((temp_X_val.value + width_pallet_x.value) > (temp_collector2_X_val.value + x_max_offset_collector.value))) || ((temp_X_val.value < (temp_collector2_X_val.value + x_min_offset_collector.value) && ((temp_X_val.value + width_pallet_x.value) > (temp_collector2_X_val.value))))) {
                    System.out.println(" collector2 0. 5 min good \n");
                    System.out.println(" collector2 max good  ----> into collector1 \n");
                    docker_range.value = false;
                    if ((temp_collector2_Y_val.value) - (temp_Y_val.value + height_pallet_y.value) < 21) {
                        temp_X_val.value = temp_X_val.value;
                        temp_Y_val.value = temp_collector2_Y_val.value - height_pallet_y.value;
                        docker_range.value = true;
                        System.out.println(" TRUE 1 \n");
                    }
                } else {
                    docker_range.value = false;
                    System.out.println(" FALSE 2 \n");
                }
                if (docker_range.value == false) {
                    if ((temp_Y_val.value + fall_speed.value) <= 612) {
                        temp_X_val.value = temp_X_val.value;
                        temp_Y_val.value = temp_Y_val.value + fall_speed.value;
                        System.out.println(" to ground base \n");
                    } else {
                        temp_X_val.value = temp_X_val.value;
                        temp_Y_val.value = 630 - height_pallet_y.value;
                        System.out.println(" on ground base \n");
                    }
                }
            }
            System.out.println(" \n\n");
            out_pallet_x.value = temp_X_val.value;
            out_pallet_y.value = temp_Y_val.value;
            out_pallet_x1.value = temp_X_val.value;
            out_pallet_y1.value = temp_Y_val.value;
            TV_out_pallet_z.value = z_pallet_mv.value;
            disp_pallet.value = true;
        } else if (dock_pallet_present.value == false) {
            System.out.println(" STILL FALSE\n");
            disp_pallet.value = false;
        }
    }
}
