package net.sf.nodeInsecure.processes;

import net.sf.nodeInsecure.computer.Machine;
import org.hibernate.validator.NotNull;
import javax.persistence.*;

/**
 * @author: janmejay.singh
 * Date: Sep 5, 2007
 * Time: 2:32:52 PM
 */
@Entity
@Table(name = "DAEMONS")
@DiscriminatorColumn(name = "DAEMON_TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("")
@org.hibernate.annotations.Entity
public class Daemon {

    public static final String INITD = "INITD", FTPD = "FTPD", SSHD = "SSHD", SMTPD = "SMTPD", DNSD = "DNSD", DHCPD = "DHCPD", HTTPD = "HTTPD", POP3D = "POP3D", IMAP4D = "IMAP4D", HTTPSD = "HttpsD", SMBD = "SMBD", MYSQLD = "MYSQLD", VNCD = "VNCD";

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Integer id;

    @NotNull
    @Column(name = "DAEMON_TYPE")
    protected String daemonType;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "MACHINE_ID")
    protected Machine machine;

    public Daemon() {
    }

    public Daemon(Machine machine, String daemonType) {
        this.machine = machine;
        this.daemonType = daemonType;
    }
}
