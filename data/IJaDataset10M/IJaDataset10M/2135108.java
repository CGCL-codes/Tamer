package org.blue.star.xdata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ListIterator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.blue.star.base.blue;
import org.blue.star.base.utils;
import org.blue.star.common.downtime;
import org.blue.star.common.objects;
import org.blue.star.include.blue_h;
import org.blue.star.include.common_h;
import org.blue.star.include.downtime_h;
import org.blue.star.include.locations_h;

public class xdddefault {

    /** Logger instance */
    private static Logger logger = LogManager.getLogger("org.blue.xdata.xdddefault");

    private static String cn = "org.blue.xdata.xdddefault";

    public static String xdddefault_downtime_file = "";

    public static String xdddefault_temp_file = "";

    public static long current_downtime_id = 0;

    public static int xdddefault_grab_config_info(String config_file) {
        blue_h.mmapfile thefile;
        xdddefault_downtime_file = locations_h.DEFAULT_DOWNTIME_FILE;
        xdddefault_temp_file = locations_h.DEFAULT_TEMP_FILE;
        thefile = utils.file_functions.mmap_fopen(config_file);
        if (thefile == null) return common_h.ERROR;
        while (true) {
            String input = utils.file_functions.mmap_fgets(thefile);
            if (input == null) break;
            input = input.trim();
            if (input.length() == 0 || input.charAt(0) == '#') continue;
            if (blue.is_core) {
                xdddefault_grab_config_directives(input);
            } else if (input.startsWith("main_config_file=")) {
                String config_file2 = input.substring(input.indexOf("=") + 1);
                blue_h.mmapfile thefile2 = utils.file_functions.mmap_fopen(config_file2);
                if (thefile2 == null) continue;
                while (true) {
                    String input2 = utils.file_functions.mmap_fgets(thefile2);
                    if (input2 == null) break;
                    input2 = input2.trim();
                    if (input2.length() == 0 || input2.charAt(0) == '#') continue;
                    xdddefault_grab_config_directives(input2);
                }
                utils.file_functions.mmap_fclose(thefile2);
            }
        }
        utils.file_functions.mmap_fclose(thefile);
        if (xdddefault_downtime_file.length() == 0) return common_h.ERROR;
        if (xdddefault_temp_file.length() == 0) return common_h.ERROR;
        if (blue.is_core) blue.macro_x[blue_h.MACRO_DOWNTIMEDATAFILE] = xdddefault_downtime_file.trim();
        return common_h.OK;
    }

    public static void xdddefault_grab_config_directives(String input_buffer) {
        if (input_buffer.startsWith("downtime_file") || input_buffer.startsWith("xdddefault_downtime_file")) {
            int index = input_buffer.indexOf("=");
            if (index < 0 || index == input_buffer.length()) return;
            xdddefault_downtime_file = input_buffer.substring(index + 1);
        } else if (input_buffer.startsWith("temp_file") || input_buffer.startsWith("xdddefault_temp_file")) {
            int index = input_buffer.indexOf("=");
            if (index < 0 || index == input_buffer.length()) return;
            xdddefault_temp_file = input_buffer.substring(index);
        }
        return;
    }

    public static int xdddefault_initialize_downtime_data(String main_config_file) {
        if (xdddefault_grab_config_info(main_config_file) == common_h.ERROR) return common_h.ERROR;
        xdddefault_create_downtime_file();
        xdddefault_read_downtime_data(main_config_file);
        xdddefault_validate_downtime_data();
        return common_h.OK;
    }

    public static int xdddefault_create_downtime_file() {
        if (new File(xdddefault_downtime_file).exists()) return common_h.OK;
        xdddefault_save_downtime_data();
        return common_h.OK;
    }

    public static int xdddefault_validate_downtime_data() {
        downtime_h.scheduled_downtime temp_downtime;
        boolean update_file = false;
        boolean save = true;
        for (ListIterator iter = downtime.scheduled_downtime_list.listIterator(); iter.hasNext(); ) {
            temp_downtime = (downtime_h.scheduled_downtime) iter.next();
            save = true;
            if (objects.find_host(temp_downtime.host_name) == null) save = false;
            if (temp_downtime.type == common_h.SERVICE_DOWNTIME && objects.find_service(temp_downtime.host_name, temp_downtime.service_description) == null) save = false;
            if (temp_downtime.end_time < utils.currentTimeInSeconds()) save = false;
            if (save == false) {
                update_file = true;
                downtime.delete_downtime(temp_downtime.type, temp_downtime.downtime_id);
            }
        }
        for (ListIterator iter = downtime.scheduled_downtime_list.listIterator(); iter.hasNext(); ) {
            temp_downtime = (downtime_h.scheduled_downtime) iter.next();
            save = true;
            if (temp_downtime.triggered_by == 0) continue;
            if (downtime.find_host_downtime(temp_downtime.triggered_by) == null && downtime.find_service_downtime(temp_downtime.triggered_by) == null) save = false;
            if (save == false) {
                update_file = true;
                downtime.delete_downtime(temp_downtime.type, temp_downtime.downtime_id);
            }
        }
        if (update_file == true) xdddefault_save_downtime_data();
        current_downtime_id = 0;
        for (ListIterator iter = downtime.scheduled_downtime_list.listIterator(); iter.hasNext(); ) {
            temp_downtime = (downtime_h.scheduled_downtime) iter.next();
            if (temp_downtime.downtime_id > current_downtime_id) current_downtime_id = temp_downtime.downtime_id;
        }
        return common_h.OK;
    }

    public static int xdddefault_cleanup_downtime_data(String main_config_file) {
        return common_h.OK;
    }

    public static downtime_h.scheduled_downtime xdddefault_add_new_host_downtime(String host_name, long entry_time, String author, String comment, long start_time, long end_time, int fixed, long triggered_by, long duration) {
        do {
            current_downtime_id++;
            if (current_downtime_id == 0) current_downtime_id++;
        } while (downtime.find_host_downtime(current_downtime_id) != null);
        downtime_h.scheduled_downtime new_downtime = downtime.add_host_downtime(host_name, entry_time, author, comment, start_time, end_time, fixed, triggered_by, duration, current_downtime_id);
        xdddefault_save_downtime_data();
        return new_downtime;
    }

    public static downtime_h.scheduled_downtime xdddefault_add_new_service_downtime(String host_name, String service_description, long entry_time, String author, String comment, long start_time, long end_time, int fixed, long triggered_by, long duration) {
        do {
            current_downtime_id++;
            if (current_downtime_id == 0) current_downtime_id++;
        } while (downtime.find_service_downtime(current_downtime_id) != null);
        downtime_h.scheduled_downtime new_downtime = downtime.add_service_downtime(host_name, service_description, entry_time, author, comment, start_time, end_time, fixed, triggered_by, duration, current_downtime_id);
        xdddefault_save_downtime_data();
        return new_downtime;
    }

    public static int xdddefault_delete_host_downtime(long downtime_id) {
        int result;
        result = xdddefault_delete_downtime(common_h.HOST_DOWNTIME, downtime_id);
        return result;
    }

    public static int xdddefault_delete_service_downtime(long downtime_id) {
        int result;
        result = xdddefault_delete_downtime(common_h.SERVICE_DOWNTIME, downtime_id);
        return result;
    }

    public static int xdddefault_delete_downtime(int type, long downtime_id) {
        xdddefault_save_downtime_data();
        return common_h.OK;
    }

    public static int xdddefault_save_downtime_data() {
        File temp_file;
        try {
            temp_file = File.createTempFile("downtime", "file");
        } catch (IOException ioE) {
            logger.fatal("Error: Unable to create temp file for writing status data!", ioE);
            return common_h.ERROR;
        }
        PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter(temp_file));
        } catch (IOException ioE) {
            logger.fatal("Error: Unable to open temp file '" + temp_file.toString() + "' for writing downtime data!", ioE);
            return common_h.ERROR;
        }
        pw.println("########################################");
        pw.println("#          NAGIOS DOWNTIME FILE");
        pw.println("#");
        pw.println("# THIS FILE IS AUTOMATICALLY GENERATED");
        pw.println("# BY NAGIOS.  DO NOT MODIFY THIS FILE!");
        pw.println("########################################");
        pw.println();
        long current_time = utils.currentTimeInSeconds();
        pw.println("info {");
        pw.println("\tcreated=" + current_time);
        pw.println("\tversion=" + common_h.PROGRAM_VERSION);
        pw.println("\t}");
        pw.println();
        for (ListIterator iter = downtime.scheduled_downtime_list.listIterator(); iter.hasNext(); ) {
            downtime_h.scheduled_downtime temp_downtime = (downtime_h.scheduled_downtime) iter.next();
            if (temp_downtime.type == common_h.HOST_DOWNTIME) pw.println("hostdowntime {"); else pw.println("servicedowntime {");
            pw.println("\thost_name=" + temp_downtime.host_name);
            if (temp_downtime.type == common_h.SERVICE_DOWNTIME) pw.println("\tservice_description=" + temp_downtime.service_description);
            pw.println("\tdowntime_id=" + temp_downtime.downtime_id);
            pw.println("\tentry_time=" + temp_downtime.entry_time);
            pw.println("\tstart_time=" + temp_downtime.start_time);
            pw.println("\tend_time=" + temp_downtime.end_time);
            pw.println("\ttriggered_by=" + temp_downtime.triggered_by);
            pw.println("\tfixed=%d" + temp_downtime.fixed);
            pw.println("\tduration=" + temp_downtime.duration);
            pw.println("\tauthor=" + temp_downtime.author);
            pw.println("\tcomment=" + temp_downtime.comment);
            pw.println("\t}");
            pw.println();
        }
        pw.close();
        if (utils.file_functions.my_rename(temp_file.toString(), xdddefault_downtime_file) != 0) {
            logger.fatal("Error: Unable to update comment data file '" + xdddefault_downtime_file + "'!");
            return common_h.ERROR;
        }
        temp_file.delete();
        return common_h.OK;
    }

    public static int xdddefault_read_downtime_data(String main_config_file) {
        String input = null;
        int data_type = xdddefault_h.XDDDEFAULT_NO_DATA;
        String var;
        String val;
        long downtime_id = 0;
        long entry_time = 0L;
        long start_time = 0L;
        long end_time = 0L;
        int fixed = common_h.FALSE;
        long triggered_by = 0;
        long duration = 0L;
        String host_name = null;
        String service_description = null;
        String comment = null;
        String author = null;
        if (!blue.is_core) if (xdddefault_grab_config_info(main_config_file) == common_h.ERROR) return common_h.ERROR;
        blue_h.mmapfile thefile = utils.file_functions.mmap_fopen(xdddefault_downtime_file);
        if (thefile == null) return common_h.ERROR;
        while (true) {
            input = utils.file_functions.mmap_fgets(thefile);
            if (input == null) break;
            input = input.trim();
            if (input.length() == 0 || input.charAt(0) == '#') continue; else if (input.equals("info {")) data_type = xdddefault_h.XDDDEFAULT_INFO_DATA; else if (input.equals("hostdowntime {")) data_type = xdddefault_h.XDDDEFAULT_HOST_DATA; else if (input.equals("servicedowntime {")) data_type = xdddefault_h.XDDDEFAULT_SERVICE_DATA; else if (input.equals("}")) {
                switch(data_type) {
                    case xdddefault_h.XDDDEFAULT_INFO_DATA:
                        break;
                    case xdddefault_h.XDDDEFAULT_HOST_DATA:
                    case xdddefault_h.XDDDEFAULT_SERVICE_DATA:
                        downtime_h.scheduled_downtime temp_downtime;
                        if (data_type == xdddefault_h.XDDDEFAULT_HOST_DATA) temp_downtime = downtime.add_host_downtime(host_name, entry_time, author, comment, start_time, end_time, fixed, triggered_by, duration, downtime_id); else temp_downtime = downtime.add_service_downtime(host_name, service_description, entry_time, author, comment, start_time, end_time, fixed, triggered_by, duration, downtime_id);
                        if (blue.is_core) downtime.register_downtime((data_type == xdddefault_h.XDDDEFAULT_HOST_DATA) ? common_h.HOST_DOWNTIME : common_h.SERVICE_DOWNTIME, temp_downtime.downtime_id);
                        break;
                    default:
                        break;
                }
                data_type = xdddefault_h.XDDDEFAULT_NO_DATA;
                host_name = null;
                service_description = null;
                author = null;
                comment = null;
                downtime_id = 0;
                entry_time = 0L;
                start_time = 0L;
                end_time = 0L;
                fixed = common_h.FALSE;
                triggered_by = 0;
                duration = 0L;
            } else if (data_type != xdddefault_h.XDDDEFAULT_NO_DATA) {
                String[] split = input.split("=", 2);
                if (split.length != 2) continue;
                var = split[0];
                val = split[1];
                switch(data_type) {
                    case xdddefault_h.XDDDEFAULT_INFO_DATA:
                        break;
                    case xdddefault_h.XDDDEFAULT_HOST_DATA:
                    case xdddefault_h.XDDDEFAULT_SERVICE_DATA:
                        if (var.equals("host_name")) host_name = val; else if (var.equals("service_description")) service_description = val; else if (var.equals("downtime_id")) downtime_id = strtoul(val, null, 10); else if (var.equals("entry_time")) entry_time = strtoul(val, null, 10); else if (var.equals("start_time")) start_time = strtoul(val, null, 10); else if (var.equals("end_time")) end_time = strtoul(val, null, 10); else if (var.equals("fixed")) fixed = (atoi(val) > 0) ? common_h.TRUE : common_h.FALSE; else if (var.equals("triggered_by")) triggered_by = strtoul(val, null, 10); else if (var.equals("duration")) duration = strtoul(val, null, 10); else if (var.equals("author")) author = val; else if (var.equals("comment")) comment = val;
                        break;
                    default:
                        break;
                }
            }
        }
        utils.file_functions.mmap_fclose(thefile);
        return common_h.OK;
    }

    private static int atoi(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfE) {
            logger.error("warning: " + nfE.getMessage(), nfE);
            return 0;
        }
    }

    private static long strtoul(String value, Object ignore, int base) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException nfE) {
            logger.error("warning: " + nfE.getMessage(), nfE);
            return 0L;
        }
    }
}
