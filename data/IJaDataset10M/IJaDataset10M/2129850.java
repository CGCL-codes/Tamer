package org.dhcpdj.config.xml.data;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="front-end" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="listen" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" default="67" />
 *                           &lt;attribute name="inet" type="{}inet-address" default="127.0.0.1" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="threads" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="max" default="10">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                                 &lt;minInclusive value="1"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="core" default="2">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                                 &lt;minInclusive value="1"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="keepalive" default="10000">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                                 &lt;minInclusive value="0"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="jdbc">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;extension base="{}empty-type">
 *                           &lt;attribute name="url" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="user" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="password" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/extension>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/all>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="global" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="server">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="identifier" use="required" type="{}inet-address" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="server-policy" type="{}policyType"/>
 *                 &lt;/all>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="topology" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element ref="{}node"/>
 *                   &lt;element ref="{}subnet"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="back-end" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="hsql" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="address" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="dbnumber" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                           &lt;attribute name="dbname" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="dbpath" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/all>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {  })
@XmlRootElement(name = "dhcp-server")
public class DhcpServer {

    @XmlElement(name = "front-end")
    protected DhcpServer.FrontEnd frontEnd;

    protected DhcpServer.Global global;

    protected DhcpServer.Topology topology;

    @XmlElement(name = "back-end")
    protected DhcpServer.BackEnd backEnd;

    /**
     * Gets the value of the frontEnd property.
     * 
     * @return
     *     possible object is
     *     {@link DhcpServer.FrontEnd }
     *     
     */
    public DhcpServer.FrontEnd getFrontEnd() {
        return frontEnd;
    }

    /**
     * Sets the value of the frontEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link DhcpServer.FrontEnd }
     *     
     */
    public void setFrontEnd(DhcpServer.FrontEnd value) {
        this.frontEnd = value;
    }

    /**
     * Gets the value of the global property.
     * 
     * @return
     *     possible object is
     *     {@link DhcpServer.Global }
     *     
     */
    public DhcpServer.Global getGlobal() {
        return global;
    }

    /**
     * Sets the value of the global property.
     * 
     * @param value
     *     allowed object is
     *     {@link DhcpServer.Global }
     *     
     */
    public void setGlobal(DhcpServer.Global value) {
        this.global = value;
    }

    /**
     * Gets the value of the topology property.
     * 
     * @return
     *     possible object is
     *     {@link DhcpServer.Topology }
     *     
     */
    public DhcpServer.Topology getTopology() {
        return topology;
    }

    /**
     * Sets the value of the topology property.
     * 
     * @param value
     *     allowed object is
     *     {@link DhcpServer.Topology }
     *     
     */
    public void setTopology(DhcpServer.Topology value) {
        this.topology = value;
    }

    /**
     * Gets the value of the backEnd property.
     * 
     * @return
     *     possible object is
     *     {@link DhcpServer.BackEnd }
     *     
     */
    public DhcpServer.BackEnd getBackEnd() {
        return backEnd;
    }

    /**
     * Sets the value of the backEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link DhcpServer.BackEnd }
     *     
     */
    public void setBackEnd(DhcpServer.BackEnd value) {
        this.backEnd = value;
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;all>
     *         &lt;element name="hsql" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="address" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="dbnumber" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *                 &lt;attribute name="dbname" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="dbpath" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/all>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {  })
    public static class BackEnd {

        protected DhcpServer.BackEnd.Hsql hsql;

        /**
         * Gets the value of the hsql property.
         * 
         * @return
         *     possible object is
         *     {@link DhcpServer.BackEnd.Hsql }
         *     
         */
        public DhcpServer.BackEnd.Hsql getHsql() {
            return hsql;
        }

        /**
         * Sets the value of the hsql property.
         * 
         * @param value
         *     allowed object is
         *     {@link DhcpServer.BackEnd.Hsql }
         *     
         */
        public void setHsql(DhcpServer.BackEnd.Hsql value) {
            this.hsql = value;
        }

        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="address" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="dbnumber" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
         *       &lt;attribute name="dbname" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="dbpath" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Hsql {

            @XmlAttribute(required = true)
            protected String address;

            @XmlAttribute(required = true)
            protected int dbnumber;

            @XmlAttribute(required = true)
            protected String dbname;

            @XmlAttribute(required = true)
            protected String dbpath;

            /**
             * Gets the value of the address property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAddress() {
                return address;
            }

            /**
             * Sets the value of the address property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAddress(String value) {
                this.address = value;
            }

            /**
             * Gets the value of the dbnumber property.
             * 
             */
            public int getDbnumber() {
                return dbnumber;
            }

            /**
             * Sets the value of the dbnumber property.
             * 
             */
            public void setDbnumber(int value) {
                this.dbnumber = value;
            }

            /**
             * Gets the value of the dbname property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDbname() {
                return dbname;
            }

            /**
             * Sets the value of the dbname property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDbname(String value) {
                this.dbname = value;
            }

            /**
             * Gets the value of the dbpath property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDbpath() {
                return dbpath;
            }

            /**
             * Sets the value of the dbpath property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDbpath(String value) {
                this.dbpath = value;
            }
        }
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;all>
     *         &lt;element name="listen" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" default="67" />
     *                 &lt;attribute name="inet" type="{}inet-address" default="127.0.0.1" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="threads" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="max" default="10">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
     *                       &lt;minInclusive value="1"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="core" default="2">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
     *                       &lt;minInclusive value="1"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="keepalive" default="10000">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
     *                       &lt;minInclusive value="0"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="jdbc">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;extension base="{}empty-type">
     *                 &lt;attribute name="url" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="user" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="password" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/extension>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/all>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {  })
    public static class FrontEnd {

        protected DhcpServer.FrontEnd.Listen listen;

        protected DhcpServer.FrontEnd.Threads threads;

        @XmlElement(required = true)
        protected DhcpServer.FrontEnd.Jdbc jdbc;

        /**
         * Gets the value of the listen property.
         * 
         * @return
         *     possible object is
         *     {@link DhcpServer.FrontEnd.Listen }
         *     
         */
        public DhcpServer.FrontEnd.Listen getListen() {
            return listen;
        }

        /**
         * Sets the value of the listen property.
         * 
         * @param value
         *     allowed object is
         *     {@link DhcpServer.FrontEnd.Listen }
         *     
         */
        public void setListen(DhcpServer.FrontEnd.Listen value) {
            this.listen = value;
        }

        /**
         * Gets the value of the threads property.
         * 
         * @return
         *     possible object is
         *     {@link DhcpServer.FrontEnd.Threads }
         *     
         */
        public DhcpServer.FrontEnd.Threads getThreads() {
            return threads;
        }

        /**
         * Sets the value of the threads property.
         * 
         * @param value
         *     allowed object is
         *     {@link DhcpServer.FrontEnd.Threads }
         *     
         */
        public void setThreads(DhcpServer.FrontEnd.Threads value) {
            this.threads = value;
        }

        /**
         * Gets the value of the jdbc property.
         * 
         * @return
         *     possible object is
         *     {@link DhcpServer.FrontEnd.Jdbc }
         *     
         */
        public DhcpServer.FrontEnd.Jdbc getJdbc() {
            return jdbc;
        }

        /**
         * Sets the value of the jdbc property.
         * 
         * @param value
         *     allowed object is
         *     {@link DhcpServer.FrontEnd.Jdbc }
         *     
         */
        public void setJdbc(DhcpServer.FrontEnd.Jdbc value) {
            this.jdbc = value;
        }

        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;extension base="{}empty-type">
         *       &lt;attribute name="url" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="user" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="password" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/extension>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Jdbc extends EmptyType {

            @XmlAttribute(required = true)
            protected String url;

            @XmlAttribute(required = true)
            protected String user;

            @XmlAttribute(required = true)
            protected String password;

            /**
             * Gets the value of the url property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getUrl() {
                return url;
            }

            /**
             * Sets the value of the url property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setUrl(String value) {
                this.url = value;
            }

            /**
             * Gets the value of the user property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getUser() {
                return user;
            }

            /**
             * Sets the value of the user property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setUser(String value) {
                this.user = value;
            }

            /**
             * Gets the value of the password property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPassword() {
                return password;
            }

            /**
             * Sets the value of the password property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPassword(String value) {
                this.password = value;
            }
        }

        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" default="67" />
         *       &lt;attribute name="inet" type="{}inet-address" default="127.0.0.1" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Listen {

            @XmlAttribute
            @XmlSchemaType(name = "unsignedShort")
            protected Integer port;

            @XmlAttribute
            @XmlJavaTypeAdapter(Adapter1.class)
            protected InetAddress inet;

            /**
             * Gets the value of the port property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public int getPort() {
                if (port == null) {
                    return 67;
                } else {
                    return port;
                }
            }

            /**
             * Sets the value of the port property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setPort(Integer value) {
                this.port = value;
            }

            /**
             * Gets the value of the inet property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public InetAddress getInet() {
                if (inet == null) {
                    return new Adapter1().unmarshal("127.0.0.1");
                } else {
                    return inet;
                }
            }

            /**
             * Sets the value of the inet property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setInet(InetAddress value) {
                this.inet = value;
            }
        }

        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="max" default="10">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
         *             &lt;minInclusive value="1"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="core" default="2">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
         *             &lt;minInclusive value="1"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="keepalive" default="10000">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
         *             &lt;minInclusive value="0"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Threads {

            @XmlAttribute
            protected Integer max;

            @XmlAttribute
            protected Integer core;

            @XmlAttribute
            protected Integer keepalive;

            /**
             * Gets the value of the max property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public int getMax() {
                if (max == null) {
                    return 10;
                } else {
                    return max;
                }
            }

            /**
             * Sets the value of the max property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setMax(Integer value) {
                this.max = value;
            }

            /**
             * Gets the value of the core property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public int getCore() {
                if (core == null) {
                    return 2;
                } else {
                    return core;
                }
            }

            /**
             * Sets the value of the core property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setCore(Integer value) {
                this.core = value;
            }

            /**
             * Gets the value of the keepalive property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public int getKeepalive() {
                if (keepalive == null) {
                    return 10000;
                } else {
                    return keepalive;
                }
            }

            /**
             * Sets the value of the keepalive property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setKeepalive(Integer value) {
                this.keepalive = value;
            }
        }
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;all>
     *         &lt;element name="server">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="identifier" use="required" type="{}inet-address" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="server-policy" type="{}policyType"/>
     *       &lt;/all>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {  })
    public static class Global {

        @XmlElement(required = true)
        protected DhcpServer.Global.Server server;

        @XmlElement(name = "server-policy", required = true)
        protected PolicyType serverPolicy;

        /**
         * Gets the value of the server property.
         * 
         * @return
         *     possible object is
         *     {@link DhcpServer.Global.Server }
         *     
         */
        public DhcpServer.Global.Server getServer() {
            return server;
        }

        /**
         * Sets the value of the server property.
         * 
         * @param value
         *     allowed object is
         *     {@link DhcpServer.Global.Server }
         *     
         */
        public void setServer(DhcpServer.Global.Server value) {
            this.server = value;
        }

        /**
         * Gets the value of the serverPolicy property.
         * 
         * @return
         *     possible object is
         *     {@link PolicyType }
         *     
         */
        public PolicyType getServerPolicy() {
            return serverPolicy;
        }

        /**
         * Sets the value of the serverPolicy property.
         * 
         * @param value
         *     allowed object is
         *     {@link PolicyType }
         *     
         */
        public void setServerPolicy(PolicyType value) {
            this.serverPolicy = value;
        }

        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="identifier" use="required" type="{}inet-address" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Server {

            @XmlAttribute(required = true)
            @XmlJavaTypeAdapter(Adapter1.class)
            protected InetAddress identifier;

            /**
             * Gets the value of the identifier property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public InetAddress getIdentifier() {
                return identifier;
            }

            /**
             * Sets the value of the identifier property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setIdentifier(InetAddress value) {
                this.identifier = value;
            }
        }
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice maxOccurs="unbounded" minOccurs="0">
     *         &lt;element ref="{}node"/>
     *         &lt;element ref="{}subnet"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "nodeOrSubnet" })
    public static class Topology {

        @XmlElements({ @XmlElement(name = "subnet", type = Subnet.class), @XmlElement(name = "node", type = Node.class) })
        protected List<TypeNodeSubnet> nodeOrSubnet;

        /**
         * Gets the value of the nodeOrSubnet property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the nodeOrSubnet property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getNodeOrSubnet().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Subnet }
         * {@link Node }
         * 
         * 
         */
        public List<TypeNodeSubnet> getNodeOrSubnet() {
            if (nodeOrSubnet == null) {
                nodeOrSubnet = new ArrayList<TypeNodeSubnet>();
            }
            return this.nodeOrSubnet;
        }
    }
}
