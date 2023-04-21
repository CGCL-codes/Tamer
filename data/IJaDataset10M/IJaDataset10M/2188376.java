package pl.swmud.ns.swmud._1_0.area;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for repairs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="repairs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="repair" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="keeper" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *                   &lt;element name="types">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="type0" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *                             &lt;element name="type1" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *                             &lt;element name="type2" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="profitfix" type="{http://www.w3.org/2001/XMLSchema}unsignedShort"/>
 *                   &lt;element name="shoptype" type="{http://www.w3.org/2001/XMLSchema}unsignedShort"/>
 *                   &lt;element name="open" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *                   &lt;element name="close" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "repairs", propOrder = { "repair" })
public class Repairs {

    protected List<Repairs.Repair> repair = new LinkedList<Repairs.Repair>();

    /**
     * Gets the value of the repair property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the repair property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRepair().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Repairs.Repair }
     * 
     * 
     */
    public List<Repairs.Repair> getRepair() {
        if (repair == null) {
            repair = new LinkedList<Repairs.Repair>();
        }
        return this.repair;
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
     *       &lt;sequence>
     *         &lt;element name="keeper" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
     *         &lt;element name="types">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="type0" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
     *                   &lt;element name="type1" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
     *                   &lt;element name="type2" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="profitfix" type="{http://www.w3.org/2001/XMLSchema}unsignedShort"/>
     *         &lt;element name="shoptype" type="{http://www.w3.org/2001/XMLSchema}unsignedShort"/>
     *         &lt;element name="open" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
     *         &lt;element name="close" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "keeper", "types", "profitfix", "shoptype", "open", "close" })
    public static class Repair {

        @XmlElement(required = true)
        protected BigInteger keeper;

        @XmlElement(required = true)
        protected Repairs.Repair.Types types;

        protected int profitfix;

        protected int shoptype;

        protected short open;

        protected short close;

        /**
         * Gets the value of the keeper property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getKeeper() {
            return keeper;
        }

        /**
         * Sets the value of the keeper property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setKeeper(BigInteger value) {
            this.keeper = value;
        }

        /**
         * Gets the value of the types property.
         * 
         * @return
         *     possible object is
         *     {@link Repairs.Repair.Types }
         *     
         */
        public Repairs.Repair.Types getTypes() {
            return types;
        }

        /**
         * Sets the value of the types property.
         * 
         * @param value
         *     allowed object is
         *     {@link Repairs.Repair.Types }
         *     
         */
        public void setTypes(Repairs.Repair.Types value) {
            this.types = value;
        }

        /**
         * Gets the value of the profitfix property.
         * 
         */
        public int getProfitfix() {
            return profitfix;
        }

        /**
         * Sets the value of the profitfix property.
         * 
         */
        public void setProfitfix(int value) {
            this.profitfix = value;
        }

        /**
         * Gets the value of the shoptype property.
         * 
         */
        public int getShoptype() {
            return shoptype;
        }

        /**
         * Sets the value of the shoptype property.
         * 
         */
        public void setShoptype(int value) {
            this.shoptype = value;
        }

        /**
         * Gets the value of the open property.
         * 
         */
        public short getOpen() {
            return open;
        }

        /**
         * Sets the value of the open property.
         * 
         */
        public void setOpen(short value) {
            this.open = value;
        }

        /**
         * Gets the value of the close property.
         * 
         */
        public short getClose() {
            return close;
        }

        /**
         * Sets the value of the close property.
         * 
         */
        public void setClose(short value) {
            this.close = value;
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
         *       &lt;sequence>
         *         &lt;element name="type0" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
         *         &lt;element name="type1" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
         *         &lt;element name="type2" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "type0", "type1", "type2" })
        public static class Types {

            protected short type0;

            protected short type1;

            protected short type2;

            /**
             * Gets the value of the type0 property.
             * 
             */
            public short getType0() {
                return type0;
            }

            /**
             * Sets the value of the type0 property.
             * 
             */
            public void setType0(short value) {
                this.type0 = value;
            }

            /**
             * Gets the value of the type1 property.
             * 
             */
            public short getType1() {
                return type1;
            }

            /**
             * Sets the value of the type1 property.
             * 
             */
            public void setType1(short value) {
                this.type1 = value;
            }

            /**
             * Gets the value of the type2 property.
             * 
             */
            public short getType2() {
                return type2;
            }

            /**
             * Sets the value of the type2 property.
             * 
             */
            public void setType2(short value) {
                this.type2 = value;
            }
        }
    }
}
