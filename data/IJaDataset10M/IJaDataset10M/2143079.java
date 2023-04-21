package schema;

import java.util.*;
import java.io.*;
import java.sql.*;

public class URLEncoder {

    public String encode(String s_in) {
        String s_out = new String(s_in);
        s_out = s_out.replace("%", "%25");
        s_out = s_out.replace(" ", "%20");
        s_out = s_out.replace("$", "%24");
        s_out = s_out.replace("&", "%26");
        s_out = s_out.replace("+", "%2B");
        s_out = s_out.replace(",", "%2C");
        s_out = s_out.replace("/", "%2F");
        s_out = s_out.replace(":", "%3A");
        s_out = s_out.replace(";", "%3B");
        s_out = s_out.replace("=", "%3D");
        s_out = s_out.replace("?", "%3F");
        s_out = s_out.replace("@", "%40");
        s_out = s_out.replace("<", "%3C");
        s_out = s_out.replace(">", "%3E");
        s_out = s_out.replace("\"", "%22");
        s_out = s_out.replace("#", "%23");
        s_out = s_out.replace("{", "%7B");
        s_out = s_out.replace("}", "%7D");
        s_out = s_out.replace("|", "%7C");
        s_out = s_out.replace("\\", "%5C");
        s_out = s_out.replace("^", "%5E");
        s_out = s_out.replace("~", "%7E");
        s_out = s_out.replace("[", "%5B");
        s_out = s_out.replace("]", "%5D");
        s_out = s_out.replace("`", "%60");
        return s_out;
    }
}
