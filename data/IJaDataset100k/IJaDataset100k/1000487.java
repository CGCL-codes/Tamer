package dinamica;

/**
 * Construye un mensaje con toda la informacion del sistema, para luego
 * ser enviada por email al destinatario configurado en el config.xml
 * Creado: 2008-08-03<br>
 * Actualizado: 2008-08-03<br>
 * Framework Din�mica - (c) 2008 Mart�n C�rdova y Asociados C.A.<br>
 * Este c�digo se distribuye bajo licencia LGPL<br>
 * @author Francisco Galizia galiziafrancisco@gmail.com
 */
public class SysInfoEmail extends SysInfo {

    @Override
    public int service(Recordset inputParams) throws Throwable {
        super.service(inputParams);
        String enabled = getConfig().getConfigValue("//mail/enabled");
        if (enabled.equals("true")) {
            Recordset rs1 = getRecordset("dbpool");
            Recordset rs2 = getRecordset("threadpool");
            Recordset rs3 = getRecordset("webappsinfo");
            Recordset rs4 = getRecordset("serverinfo");
            Recordset rs5 = getRecordset("threaddump");
            String host = getConfig().getConfigValue("//mail/host");
            String subject = getConfig().getConfigValue("//mail/subject");
            String from = getConfig().getConfigValue("//mail/from");
            String fromName = getConfig().getConfigValue("//mail/from-name");
            ;
            String to = getConfig().getConfigValue("//mail/to");
            String body = getResource("body.txt");
            TemplateEngine t = new TemplateEngine(getContext(), getRequest(), body);
            t.replaceDefaultValues();
            t.replaceLabels();
            t.replaceRequestAttributes();
            t.replace(rs1, "", "pool");
            t.replace(rs2, "", "thread");
            t.replace(rs3, "", "webapp");
            t.replace(rs5, "", "threaddump");
            t.replace(rs4, "");
            body = t.toString();
            SimpleMail s = new SimpleMail();
            s.send(host, from, fromName, to, subject, body);
        }
        return 0;
    }
}
