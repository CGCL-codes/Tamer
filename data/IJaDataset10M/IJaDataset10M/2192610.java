package game;

import java.io.IOException;

public class Enemy_Attributes extends Attributes {

    protected int mhp;

    protected short exp;

    protected short mmp;

    protected short gld;

    protected String idc;

    protected String idr;

    public Enemy_Attributes() {
    }

    public Enemy_Attributes(String e_name) {
        super("enem_" + e_name);
    }

    protected void read_data() {
        try {
            string_returned_length = line_temp.length();
            mhp = htp = Integer.parseInt(line_temp.substring(4, string_returned_length));
            line_temp = attri_file.readLine();
            string_returned_length = line_temp.length();
            mmp = mgp = Short.parseShort(line_temp.substring(4, string_returned_length));
            line_temp = attri_file.readLine();
            string_returned_length = line_temp.length();
            exp = Short.parseShort(line_temp.substring(4, string_returned_length));
            line_temp = attri_file.readLine();
            string_returned_length = line_temp.length();
            fat = Short.parseShort(line_temp.substring(4, string_returned_length));
            line_temp = attri_file.readLine();
            string_returned_length = line_temp.length();
            str = Short.parseShort(line_temp.substring(4, string_returned_length));
            line_temp = attri_file.readLine();
            string_returned_length = line_temp.length();
            def = Short.parseShort(line_temp.substring(4, string_returned_length));
            line_temp = attri_file.readLine();
            string_returned_length = line_temp.length();
            agl = Short.parseShort(line_temp.substring(4, string_returned_length));
            line_temp = attri_file.readLine();
            string_returned_length = line_temp.length();
            acc = Short.parseShort(line_temp.substring(4, string_returned_length));
            line_temp = attri_file.readLine();
            string_returned_length = line_temp.length();
            vit = Short.parseShort(line_temp.substring(4, string_returned_length));
            line_temp = attri_file.readLine();
            string_returned_length = line_temp.length();
            inl = Short.parseShort(line_temp.substring(4, string_returned_length));
            line_temp = attri_file.readLine();
            string_returned_length = line_temp.length();
            gld = Short.parseShort(line_temp.substring(4, string_returned_length));
            line_temp = attri_file.readLine();
            string_returned_length = line_temp.length();
            idc = line_temp.substring(4, string_returned_length);
            line_temp = attri_file.readLine();
            string_returned_length = line_temp.length();
            idr = line_temp.substring(4, string_returned_length);
        } catch (IOException e) {
            System.out.println("I lost it...");
        }
    }

    public void htp_mod(int hmod) {
        htp += hmod;
        if (htp < 0) htp = 0;
        if (htp > mhp) htp = mhp;
    }

    public void mgp_mod(int mmod) {
        mgp += mmod;
        if (mgp < 0) mgp = 0;
        if (mgp > mmp) mgp = mmp;
    }

    public void fat_mod(int fmod) {
        fat += fmod;
        if (fat < 0) fat = 0;
        if (fat > 100) fat = 100;
    }

    public short get_exp() {
        return exp;
    }

    public String get_item_com() {
        return idc;
    }

    public String get_item_rar() {
        return idr;
    }
}
