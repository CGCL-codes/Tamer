package cn.myapps.base.action;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  nicholas
 */
public class BlobFileObject {

    private static final Map MIME_TYPE_MAP = new HashMap();

    static {
        MIME_TYPE_MAP.put(".001", "application/x-001");
        MIME_TYPE_MAP.put(".301", "application/x-301");
        MIME_TYPE_MAP.put(".323", "text/h323");
        MIME_TYPE_MAP.put(".906", "application/x-906");
        MIME_TYPE_MAP.put(".907", "drawing/907");
        MIME_TYPE_MAP.put(".a11", "application/x-a11");
        MIME_TYPE_MAP.put(".acp", "audio/x-mei-aac");
        MIME_TYPE_MAP.put(".ai", "application/postscript");
        MIME_TYPE_MAP.put(".aif", "audio/aiff");
        MIME_TYPE_MAP.put(".aifc", "audio/aiff");
        MIME_TYPE_MAP.put(".aiff", "audio/aiff");
        MIME_TYPE_MAP.put(".anv", "application/x-anv");
        MIME_TYPE_MAP.put(".asa", "text/asa");
        MIME_TYPE_MAP.put(".asf", "video/x-ms-asf");
        MIME_TYPE_MAP.put(".asp", "text/asp");
        MIME_TYPE_MAP.put(".asx", "video/x-ms-asf");
        MIME_TYPE_MAP.put(".au", "audio/basic");
        MIME_TYPE_MAP.put(".avi", "video/avi");
        MIME_TYPE_MAP.put(".awf", "application/vnd.adobe.workflow");
        MIME_TYPE_MAP.put(".biz", "text/xml");
        MIME_TYPE_MAP.put(".bmp", "application/x-bmp");
        MIME_TYPE_MAP.put(".bot", "application/x-bot");
        MIME_TYPE_MAP.put(".c4t", "application/x-c4t");
        MIME_TYPE_MAP.put(".c90", "application/x-c90");
        MIME_TYPE_MAP.put(".cal", "application/x-cals");
        MIME_TYPE_MAP.put(".cat", "application/vnd.ms-pki.seccat");
        MIME_TYPE_MAP.put(".cdf", "application/x-netcdf");
        MIME_TYPE_MAP.put(".cdr", "application/x-cdr");
        MIME_TYPE_MAP.put(".cel", "application/x-cel");
        MIME_TYPE_MAP.put(".cer", "application/x-x509-ca-cert");
        MIME_TYPE_MAP.put(".cg4", "application/x-g4");
        MIME_TYPE_MAP.put(".cgm", "application/x-cgm");
        MIME_TYPE_MAP.put(".cit", "application/x-cit");
        MIME_TYPE_MAP.put(".class", "java/*");
        MIME_TYPE_MAP.put(".cml", "text/xml");
        MIME_TYPE_MAP.put(".cmp", "application/x-cmp");
        MIME_TYPE_MAP.put(".cmx", "application/x-cmx");
        MIME_TYPE_MAP.put(".cot", "application/x-cot");
        MIME_TYPE_MAP.put(".crl", "application/pkix-crl");
        MIME_TYPE_MAP.put(".crt", "application/x-x509-ca-cert");
        MIME_TYPE_MAP.put(".csi", "application/x-csi");
        MIME_TYPE_MAP.put(".css", "text/css");
        MIME_TYPE_MAP.put(".cut", "application/x-cut");
        MIME_TYPE_MAP.put(".dbf", "application/x-dbf");
        MIME_TYPE_MAP.put(".dbm", "application/x-dbm");
        MIME_TYPE_MAP.put(".dbx", "application/x-dbx");
        MIME_TYPE_MAP.put(".dcd", "text/xml");
        MIME_TYPE_MAP.put(".dcx", "application/x-dcx");
        MIME_TYPE_MAP.put(".der", "application/x-x509-ca-cert");
        MIME_TYPE_MAP.put(".dgn", "application/x-dgn");
        MIME_TYPE_MAP.put(".dib", "application/x-dib");
        MIME_TYPE_MAP.put(".dll", "application/x-msdownload");
        MIME_TYPE_MAP.put(".doc", "application/msword");
        MIME_TYPE_MAP.put(".dot", "application/msword");
        MIME_TYPE_MAP.put(".drw", "application/x-drw");
        MIME_TYPE_MAP.put(".dtd", "text/xml");
        MIME_TYPE_MAP.put(".dwf", "Model/vnd.dwf");
        MIME_TYPE_MAP.put(".dwf", "application/x-dwf");
        MIME_TYPE_MAP.put(".dwg", "application/x-dwg");
        MIME_TYPE_MAP.put(".dxb", "application/x-dxb");
        MIME_TYPE_MAP.put(".dxf", "application/x-dxf");
        MIME_TYPE_MAP.put(".edn", "application/vnd.adobe.edn");
        MIME_TYPE_MAP.put(".emf", "application/x-emf");
        MIME_TYPE_MAP.put(".eml", "message/rfc822");
        MIME_TYPE_MAP.put(".ent", "text/xml");
        MIME_TYPE_MAP.put(".epi", "application/x-epi");
        MIME_TYPE_MAP.put(".eps", "application/x-ps");
        MIME_TYPE_MAP.put(".eps", "application/postscript");
        MIME_TYPE_MAP.put(".etd", "application/x-ebx");
        MIME_TYPE_MAP.put(".exe", "application/x-msdownload");
        MIME_TYPE_MAP.put(".fax", "image/fax");
        MIME_TYPE_MAP.put(".fdf", "application/vnd.fdf");
        MIME_TYPE_MAP.put(".fif", "application/fractals");
        MIME_TYPE_MAP.put(".fo", "text/xml");
        MIME_TYPE_MAP.put(".frm", "application/x-frm");
        MIME_TYPE_MAP.put(".g4", "application/x-g4");
        MIME_TYPE_MAP.put(".gbr", "application/x-gbr");
        MIME_TYPE_MAP.put(".gcd", "application/x-gcd");
        MIME_TYPE_MAP.put(".gif", "image/gif");
        MIME_TYPE_MAP.put(".gl2", "application/x-gl2");
        MIME_TYPE_MAP.put(".gp4", "application/x-gp4");
        MIME_TYPE_MAP.put(".hgl", "application/x-hgl");
        MIME_TYPE_MAP.put(".hmr", "application/x-hmr");
        MIME_TYPE_MAP.put(".hpg", "application/x-hpgl");
        MIME_TYPE_MAP.put(".hpl", "application/x-hpl");
        MIME_TYPE_MAP.put(".hqx", "application/mac-binhex40");
        MIME_TYPE_MAP.put(".hrf", "application/x-hrf");
        MIME_TYPE_MAP.put(".hta", "application/hta");
        MIME_TYPE_MAP.put(".htc", "text/x-component");
        MIME_TYPE_MAP.put(".htm", "text/html");
        MIME_TYPE_MAP.put(".html", "text/html");
        MIME_TYPE_MAP.put(".htt", "text/webviewhtml");
        MIME_TYPE_MAP.put(".htx", "text/html");
        MIME_TYPE_MAP.put(".icb", "application/x-icb");
        MIME_TYPE_MAP.put(".ico", "image/x-icon");
        MIME_TYPE_MAP.put(".ico", "application/x-ico");
        MIME_TYPE_MAP.put(".iff", "application/x-iff");
        MIME_TYPE_MAP.put(".ig4", "application/x-g4");
        MIME_TYPE_MAP.put(".igs", "application/x-igs");
        MIME_TYPE_MAP.put(".iii", "application/x-iphone");
        MIME_TYPE_MAP.put(".img", "application/x-img");
        MIME_TYPE_MAP.put(".ins", "application/x-internet-signup");
        MIME_TYPE_MAP.put(".isp", "application/x-internet-signup");
        MIME_TYPE_MAP.put(".IVF", "video/x-ivf");
        MIME_TYPE_MAP.put(".java", "java/*");
        MIME_TYPE_MAP.put(".jfif", "image/jpeg");
        MIME_TYPE_MAP.put(".jpe", "image/jpeg");
        MIME_TYPE_MAP.put(".jpe", "application/x-jpe");
        MIME_TYPE_MAP.put(".jpeg", "image/jpeg");
        MIME_TYPE_MAP.put(".jpg", "image/jpeg");
        MIME_TYPE_MAP.put(".jpg", "application/x-jpg");
        MIME_TYPE_MAP.put(".js", "application/x-javascript");
        MIME_TYPE_MAP.put(".jsp", "text/html");
        MIME_TYPE_MAP.put(".la1", "audio/x-liquid-file");
        MIME_TYPE_MAP.put(".lar", "application/x-laplayer-reg");
        MIME_TYPE_MAP.put(".latex", "application/x-latex");
        MIME_TYPE_MAP.put(".lavs", "audio/x-liquid-secure");
        MIME_TYPE_MAP.put(".lbm", "application/x-lbm");
        MIME_TYPE_MAP.put(".lmsff", "audio/x-la-lms");
        MIME_TYPE_MAP.put(".ls", "application/x-javascript");
        MIME_TYPE_MAP.put(".ltr", "application/x-ltr");
        MIME_TYPE_MAP.put(".m1v", "video/x-mpeg");
        MIME_TYPE_MAP.put(".m2v", "video/x-mpeg");
        MIME_TYPE_MAP.put(".m3u", "audio/mpegurl");
        MIME_TYPE_MAP.put(".m4e", "video/mpeg4");
        MIME_TYPE_MAP.put(".mac", "application/x-mac");
        MIME_TYPE_MAP.put(".man", "application/x-troff-man");
        MIME_TYPE_MAP.put(".math", "text/xml");
        MIME_TYPE_MAP.put(".mdb", "application/msaccess");
        MIME_TYPE_MAP.put(".mdb", "application/x-mdb");
        MIME_TYPE_MAP.put(".mfp", "application/x-shockwave-flash");
        MIME_TYPE_MAP.put(".mht", "message/rfc822");
        MIME_TYPE_MAP.put(".mhtml", "message/rfc822");
        MIME_TYPE_MAP.put(".mi", "application/x-mi");
        MIME_TYPE_MAP.put(".mid", "audio/mid");
        MIME_TYPE_MAP.put(".midi", "audio/mid");
        MIME_TYPE_MAP.put(".mil", "application/x-mil");
        MIME_TYPE_MAP.put(".mml", "text/xml");
        MIME_TYPE_MAP.put(".mnd", "audio/x-musicnet-download");
        MIME_TYPE_MAP.put(".mns", "audio/x-musicnet-stream");
        MIME_TYPE_MAP.put(".mocha", "application/x-javascript");
        MIME_TYPE_MAP.put(".movie", "video/x-sgi-movie");
        MIME_TYPE_MAP.put(".mp1", "audio/mp1");
        MIME_TYPE_MAP.put(".mp2", "audio/mp2");
        MIME_TYPE_MAP.put(".mp2v", "video/mpeg");
        MIME_TYPE_MAP.put(".mp3", "audio/mp3");
        MIME_TYPE_MAP.put(".mp4", "video/mpeg4");
        MIME_TYPE_MAP.put(".mpa", "video/x-mpg");
        MIME_TYPE_MAP.put(".mpd", "application/vnd.ms-project");
        MIME_TYPE_MAP.put(".mpe", "video/x-mpeg");
        MIME_TYPE_MAP.put(".mpeg", "video/mpg");
        MIME_TYPE_MAP.put(".mpg", "video/mpg");
        MIME_TYPE_MAP.put(".mpga", "audio/rn-mpeg");
        MIME_TYPE_MAP.put(".mpp", "application/vnd.ms-project");
        MIME_TYPE_MAP.put(".mps", "video/x-mpeg");
        MIME_TYPE_MAP.put(".mpt", "application/vnd.ms-project");
        MIME_TYPE_MAP.put(".mpv", "video/mpg");
        MIME_TYPE_MAP.put(".mpv2", "video/mpeg");
        MIME_TYPE_MAP.put(".mpw", "application/vnd.ms-project");
        MIME_TYPE_MAP.put(".mpx", "application/vnd.ms-project");
        MIME_TYPE_MAP.put(".mtx", "text/xml");
        MIME_TYPE_MAP.put(".mxp", "application/x-mmxp");
        MIME_TYPE_MAP.put(".net", "image/pnetvue");
        MIME_TYPE_MAP.put(".nrf", "application/x-nrf");
        MIME_TYPE_MAP.put(".nws", "message/rfc822");
        MIME_TYPE_MAP.put(".odc", "text/x-ms-odc");
        MIME_TYPE_MAP.put(".out", "application/x-out");
        MIME_TYPE_MAP.put(".p10", "application/pkcs10");
        MIME_TYPE_MAP.put(".p12", "application/x-pkcs12");
        MIME_TYPE_MAP.put(".p7b", "application/x-pkcs7-certificates");
        MIME_TYPE_MAP.put(".p7c", "application/pkcs7-mime");
        MIME_TYPE_MAP.put(".p7m", "application/pkcs7-mime");
        MIME_TYPE_MAP.put(".p7r", "application/x-pkcs7-certreqresp");
        MIME_TYPE_MAP.put(".p7s", "application/pkcs7-signature");
        MIME_TYPE_MAP.put(".pc5", "application/x-pc5");
        MIME_TYPE_MAP.put(".pci", "application/x-pci");
        MIME_TYPE_MAP.put(".pcl", "application/x-pcl");
        MIME_TYPE_MAP.put(".pcx", "application/x-pcx");
        MIME_TYPE_MAP.put(".pdf", "application/pdf");
        MIME_TYPE_MAP.put(".pdf", "application/pdf");
        MIME_TYPE_MAP.put(".pdx", "application/vnd.adobe.pdx");
        MIME_TYPE_MAP.put(".pfx", "application/x-pkcs12");
        MIME_TYPE_MAP.put(".pgl", "application/x-pgl");
        MIME_TYPE_MAP.put(".pic", "application/x-pic");
        MIME_TYPE_MAP.put(".pko", "application/vnd.ms-pki.pko");
        MIME_TYPE_MAP.put(".pl", "application/x-perl");
        MIME_TYPE_MAP.put(".plg", "text/html");
        MIME_TYPE_MAP.put(".pls", "audio/scpls");
        MIME_TYPE_MAP.put(".plt", "application/x-plt");
        MIME_TYPE_MAP.put(".png", "image/png");
        MIME_TYPE_MAP.put(".png", "application/x-png");
        MIME_TYPE_MAP.put(".pot", "application/vnd.ms-powerpoint");
        MIME_TYPE_MAP.put(".ppa", "application/vnd.ms-powerpoint");
        MIME_TYPE_MAP.put(".ppm", "application/x-ppm");
        MIME_TYPE_MAP.put(".pps", "application/vnd.ms-powerpoint");
        MIME_TYPE_MAP.put(".ppt", "application/vnd.ms-powerpoint");
        MIME_TYPE_MAP.put(".ppt", "application/x-ppt");
        MIME_TYPE_MAP.put(".pr", "application/x-pr");
        MIME_TYPE_MAP.put(".prf", "application/pics-rules");
        MIME_TYPE_MAP.put(".prn", "application/x-prn");
        MIME_TYPE_MAP.put(".prt", "application/x-prt");
        MIME_TYPE_MAP.put(".ps", "application/x-ps");
        MIME_TYPE_MAP.put(".ps", "application/postscript");
        MIME_TYPE_MAP.put(".ptn", "application/x-ptn");
        MIME_TYPE_MAP.put(".pwz", "application/vnd.ms-powerpoint");
        MIME_TYPE_MAP.put(".r3t", "text/vnd.rn-realtext3d");
        MIME_TYPE_MAP.put(".ra", "audio/vnd.rn-realaudio");
        MIME_TYPE_MAP.put(".ram", "audio/x-pn-realaudio");
        MIME_TYPE_MAP.put(".ras", "application/x-ras");
        MIME_TYPE_MAP.put(".rat", "application/rat-file");
        MIME_TYPE_MAP.put(".rdf", "text/xml");
        MIME_TYPE_MAP.put(".rec", "application/vnd.rn-recording");
        MIME_TYPE_MAP.put(".red", "application/x-red");
        MIME_TYPE_MAP.put(".rgb", "application/x-rgb");
        MIME_TYPE_MAP.put(".rjs", "application/vnd.rn-realsystem-rjs");
        MIME_TYPE_MAP.put(".rjt", "application/vnd.rn-realsystem-rjt");
        MIME_TYPE_MAP.put(".rlc", "application/x-rlc");
        MIME_TYPE_MAP.put(".rle", "application/x-rle");
        MIME_TYPE_MAP.put(".rm", "application/vnd.rn-realmedia");
        MIME_TYPE_MAP.put(".rmf", "application/vnd.adobe.rmf");
        MIME_TYPE_MAP.put(".rmi", "audio/mid");
        MIME_TYPE_MAP.put(".rmj", "application/vnd.rn-realsystem-rmj");
        MIME_TYPE_MAP.put(".rmm", "audio/x-pn-realaudio");
        MIME_TYPE_MAP.put(".rmp", "application/vnd.rn-rn_music_package");
        MIME_TYPE_MAP.put(".rms", "application/vnd.rn-realmedia-secure");
        MIME_TYPE_MAP.put(".rmvb", "application/vnd.rn-realmedia-vbr");
        MIME_TYPE_MAP.put(".rmx", "application/vnd.rn-realsystem-rmx");
        MIME_TYPE_MAP.put(".rnx", "application/vnd.rn-realplayer");
        MIME_TYPE_MAP.put(".rp", "image/vnd.rn-realpix");
        MIME_TYPE_MAP.put(".rpm", "audio/x-pn-realaudio-plugin");
        MIME_TYPE_MAP.put(".rsml", "application/vnd.rn-rsml");
        MIME_TYPE_MAP.put(".rt", "text/vnd.rn-realtext");
        MIME_TYPE_MAP.put(".rtf", "application/msword");
        MIME_TYPE_MAP.put(".rtf", "application/x-rtf");
        MIME_TYPE_MAP.put(".rv", "video/vnd.rn-realvideo");
        MIME_TYPE_MAP.put(".sam", "application/x-sam");
        MIME_TYPE_MAP.put(".sat", "application/x-sat");
        MIME_TYPE_MAP.put(".sdp", "application/sdp");
        MIME_TYPE_MAP.put(".sdw", "application/x-sdw");
        MIME_TYPE_MAP.put(".sit", "application/x-stuffit");
        MIME_TYPE_MAP.put(".slb", "application/x-slb");
        MIME_TYPE_MAP.put(".sld", "application/x-sld");
        MIME_TYPE_MAP.put(".slk", "drawing/x-slk");
        MIME_TYPE_MAP.put(".smi", "application/smil");
        MIME_TYPE_MAP.put(".smil", "application/smil");
        MIME_TYPE_MAP.put(".smk", "application/x-smk");
        MIME_TYPE_MAP.put(".snd", "audio/basic");
        MIME_TYPE_MAP.put(".sol", "text/plain");
        MIME_TYPE_MAP.put(".sor", "text/plain");
        MIME_TYPE_MAP.put(".spc", "application/x-pkcs7-certificates");
        MIME_TYPE_MAP.put(".spl", "application/futuresplash");
        MIME_TYPE_MAP.put(".spp", "text/xml");
        MIME_TYPE_MAP.put(".ssm", "application/streamingmedia");
        MIME_TYPE_MAP.put(".sst", "application/vnd.ms-pki.certstore");
        MIME_TYPE_MAP.put(".stl", "application/vnd.ms-pki.stl");
        MIME_TYPE_MAP.put(".stm", "text/html");
        MIME_TYPE_MAP.put(".sty", "application/x-sty");
        MIME_TYPE_MAP.put(".svg", "text/xml");
        MIME_TYPE_MAP.put(".swf", "application/x-shockwave-flash");
        MIME_TYPE_MAP.put(".tdf", "application/x-tdf");
        MIME_TYPE_MAP.put(".tg4", "application/x-tg4");
        MIME_TYPE_MAP.put(".tga", "application/x-tga");
        MIME_TYPE_MAP.put(".tif", "image/tiff");
        MIME_TYPE_MAP.put(".tif", "application/x-tif");
        MIME_TYPE_MAP.put(".tiff", "image/tiff");
        MIME_TYPE_MAP.put(".tld", "text/xml");
        MIME_TYPE_MAP.put(".top", "drawing/x-top");
        MIME_TYPE_MAP.put(".torrent", "application/x-bittorrent");
        MIME_TYPE_MAP.put(".tsd", "text/xml");
        MIME_TYPE_MAP.put(".txt", "text/plain");
        MIME_TYPE_MAP.put(".uin", "application/x-icq");
        MIME_TYPE_MAP.put(".uls", "text/iuls");
        MIME_TYPE_MAP.put(".vcf", "text/x-vcard");
        MIME_TYPE_MAP.put(".vda", "application/x-vda");
        MIME_TYPE_MAP.put(".vdx", "application/vnd.visio");
        MIME_TYPE_MAP.put(".vml", "text/xml");
        MIME_TYPE_MAP.put(".vpg", "application/x-vpeg005");
        MIME_TYPE_MAP.put(".vsd", "application/vnd.visio");
        MIME_TYPE_MAP.put(".vsd", "application/x-vsd");
        MIME_TYPE_MAP.put(".vss", "application/vnd.visio");
        MIME_TYPE_MAP.put(".vst", "application/vnd.visio");
        MIME_TYPE_MAP.put(".vst", "application/x-vst");
        MIME_TYPE_MAP.put(".vsw", "application/vnd.visio");
        MIME_TYPE_MAP.put(".vsx", "application/vnd.visio");
        MIME_TYPE_MAP.put(".vtx", "application/vnd.visio");
        MIME_TYPE_MAP.put(".vxml", "text/xml");
        MIME_TYPE_MAP.put(".wav", "audio/wav");
        MIME_TYPE_MAP.put(".wax", "audio/x-ms-wax");
        MIME_TYPE_MAP.put(".wb1", "application/x-wb1");
        MIME_TYPE_MAP.put(".wb2", "application/x-wb2");
        MIME_TYPE_MAP.put(".wb3", "application/x-wb3");
        MIME_TYPE_MAP.put(".wbmp", "image/vnd.wap.wbmp");
        MIME_TYPE_MAP.put(".wiz", "application/msword");
        MIME_TYPE_MAP.put(".wk3", "application/x-wk3");
        MIME_TYPE_MAP.put(".wk4", "application/x-wk4");
        MIME_TYPE_MAP.put(".wkq", "application/x-wkq");
        MIME_TYPE_MAP.put(".wks", "application/x-wks");
        MIME_TYPE_MAP.put(".wm", "video/x-ms-wm");
        MIME_TYPE_MAP.put(".wma", "audio/x-ms-wma");
        MIME_TYPE_MAP.put(".wmd", "application/x-ms-wmd");
        MIME_TYPE_MAP.put(".wmf", "application/x-wmf");
        MIME_TYPE_MAP.put(".wml", "text/vnd.wap.wml");
        MIME_TYPE_MAP.put(".wmv", "video/x-ms-wmv");
        MIME_TYPE_MAP.put(".wmx", "video/x-ms-wmx");
        MIME_TYPE_MAP.put(".wmz", "application/x-ms-wmz");
        MIME_TYPE_MAP.put(".wp6", "application/x-wp6");
        MIME_TYPE_MAP.put(".wpd", "application/x-wpd");
        MIME_TYPE_MAP.put(".wpg", "application/x-wpg");
        MIME_TYPE_MAP.put(".wpl", "application/vnd.ms-wpl");
        MIME_TYPE_MAP.put(".wq1", "application/x-wq1");
        MIME_TYPE_MAP.put(".wr1", "application/x-wr1");
        MIME_TYPE_MAP.put(".wri", "application/x-wri");
        MIME_TYPE_MAP.put(".wrk", "application/x-wrk");
        MIME_TYPE_MAP.put(".ws", "application/x-ws");
        MIME_TYPE_MAP.put(".ws2", "application/x-ws");
        MIME_TYPE_MAP.put(".wsc", "text/scriptlet");
        MIME_TYPE_MAP.put(".wsdl", "text/xml");
        MIME_TYPE_MAP.put(".wvx", "video/x-ms-wvx");
        MIME_TYPE_MAP.put(".xdp", "application/vnd.adobe.xdp");
        MIME_TYPE_MAP.put(".xdr", "text/xml");
        MIME_TYPE_MAP.put(".xfd", "application/vnd.adobe.xfd");
        MIME_TYPE_MAP.put(".xfdf", "application/vnd.adobe.xfdf");
        MIME_TYPE_MAP.put(".xhtml", "text/html");
        MIME_TYPE_MAP.put(".xls", "application/vnd.ms-excel");
        MIME_TYPE_MAP.put(".xls", "application/x-xls");
        MIME_TYPE_MAP.put(".xlw", "application/x-xlw");
        MIME_TYPE_MAP.put(".xml", "text/xml");
        MIME_TYPE_MAP.put(".xpl", "audio/scpls");
        MIME_TYPE_MAP.put(".xq", "text/xml");
        MIME_TYPE_MAP.put(".xql", "text/xml");
        MIME_TYPE_MAP.put(".xquery", "text/xml");
        MIME_TYPE_MAP.put(".xsd", "text/xml");
        MIME_TYPE_MAP.put(".xsl", "text/xml");
        MIME_TYPE_MAP.put(".xslt", "text/xml");
        MIME_TYPE_MAP.put(".xwd", "application/x-xwd");
        MIME_TYPE_MAP.put(".x_b", "application/x-x_b");
        MIME_TYPE_MAP.put(".x_t", "application/x-x_t");
    }

    /**
	 * @uml.property  name="content"
	 */
    private byte[] content;

    /**
	 * @uml.property  name="fullfilename"
	 */
    private String fullfilename;

    public BlobFileObject() {
    }

    public BlobFileObject(String fullfilename, byte[] content) {
        this.fullfilename = fullfilename;
        this.content = content;
    }

    /**
	 * @return  Returns the content.
	 * @uml.property  name="content"
	 */
    public byte[] getContent() {
        return content;
    }

    /**
	 * @param content  the content to set
	 * @uml.property  name="content"
	 */
    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
	 * @return Returns the extense.
	 */
    public String getExtense() {
        return getExtense(this.fullfilename);
    }

    /**
	 * @return Returns the filename.
	 */
    public String getFilename() {
        return getLogicFileName(this.fullfilename);
    }

    /**
	 * Get the file logic name.
	 * @param fullname The file full name
	 * @return The full name.
	 */
    public static String getLogicFileName(String fullname) {
        return (fullname != null && fullname.length() > 0) ? fullname.substring(fullname.lastIndexOf("-") + 1) : "";
    }

    /**
	 * Get the file extense
	 * @param fullname The file full name.
	 * @return The file extense.
	 */
    public static String getExtense(String fullname) {
        return (fullname != null && fullname.length() > 0) ? fullname.substring(fullname.lastIndexOf(".")) : "";
    }

    /**
	 * @return  Returns the fullfilename.
	 * @uml.property  name="fullfilename"
	 */
    public String getFullfilename() {
        return fullfilename;
    }

    /**
	 * @param fullfilename  The fullfilename to set.
	 * @uml.property  name="fullfilename"
	 */
    public void setFullfilename(String fullfilename) {
        this.fullfilename = fullfilename;
    }

    /**
	 * @return The content type.
	 */
    public String getContentType() {
        String extesion = getExtense();
        if (extesion != null) {
            String key = extesion.trim().toLowerCase();
            String value = (String) MIME_TYPE_MAP.get(key);
            if (value != null) {
                return value;
            }
        }
        return "application/octet-stream";
    }
}
