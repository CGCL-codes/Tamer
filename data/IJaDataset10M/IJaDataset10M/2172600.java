package transcript;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element name="transcriptRecord">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="university" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="degree" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="year" type="{http://xml.netbeans.org/schema/commonSchema}year"/>
 *                   &lt;element name="major" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="courses">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="course" maxOccurs="unbounded">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="code" type="{http://xml.netbeans.org/schema/commonSchema}courseCode"/>
 *                                       &lt;element name="credits" type="{http://xml.netbeans.org/schema/commonSchema}credits"/>
 *                                       &lt;element name="grade" type="{http://xml.netbeans.org/schema/commonSchema}grade"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="personalNumber" type="{http://www.w3.org/2001/XMLSchema}string" />
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
@XmlType(name = "", propOrder = { "transcriptRecord" })
@XmlRootElement(name = "transcript", namespace = "http://xml.netbeans.org/schema/transcriptSchema")
public class Transcript {

    @XmlElement(namespace = "http://xml.netbeans.org/schema/transcriptSchema", required = true)
    protected Transcript.TranscriptRecord transcriptRecord;

    /**
     * Gets the value of the transcriptRecord property.
     * 
     * @return
     *     possible object is
     *     {@link Transcript.TranscriptRecord }
     *     
     */
    public Transcript.TranscriptRecord getTranscriptRecord() {
        return transcriptRecord;
    }

    /**
     * Sets the value of the transcriptRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link Transcript.TranscriptRecord }
     *     
     */
    public void setTranscriptRecord(Transcript.TranscriptRecord value) {
        this.transcriptRecord = value;
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
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="university" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="degree" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="year" type="{http://xml.netbeans.org/schema/commonSchema}year"/>
     *         &lt;element name="major" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="courses">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="course" maxOccurs="unbounded">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="code" type="{http://xml.netbeans.org/schema/commonSchema}courseCode"/>
     *                             &lt;element name="credits" type="{http://xml.netbeans.org/schema/commonSchema}credits"/>
     *                             &lt;element name="grade" type="{http://xml.netbeans.org/schema/commonSchema}grade"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="personalNumber" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "name", "university", "degree", "year", "major", "courses" })
    public static class TranscriptRecord {

        @XmlElement(namespace = "http://xml.netbeans.org/schema/transcriptSchema", required = true)
        protected String name;

        @XmlElement(namespace = "http://xml.netbeans.org/schema/transcriptSchema", required = true)
        protected String university;

        @XmlElement(namespace = "http://xml.netbeans.org/schema/transcriptSchema", required = true)
        protected String degree;

        @XmlElement(namespace = "http://xml.netbeans.org/schema/transcriptSchema")
        protected int year;

        @XmlElement(namespace = "http://xml.netbeans.org/schema/transcriptSchema", required = true)
        protected String major;

        @XmlElement(namespace = "http://xml.netbeans.org/schema/transcriptSchema", required = true)
        protected Transcript.TranscriptRecord.Courses courses;

        @XmlAttribute(name = "personalNumber")
        protected String personalNumber;

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the university property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUniversity() {
            return university;
        }

        /**
         * Sets the value of the university property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUniversity(String value) {
            this.university = value;
        }

        /**
         * Gets the value of the degree property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDegree() {
            return degree;
        }

        /**
         * Sets the value of the degree property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDegree(String value) {
            this.degree = value;
        }

        /**
         * Gets the value of the year property.
         * 
         */
        public int getYear() {
            return year;
        }

        /**
         * Sets the value of the year property.
         * 
         */
        public void setYear(int value) {
            this.year = value;
        }

        /**
         * Gets the value of the major property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMajor() {
            return major;
        }

        /**
         * Sets the value of the major property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMajor(String value) {
            this.major = value;
        }

        /**
         * Gets the value of the courses property.
         * 
         * @return
         *     possible object is
         *     {@link Transcript.TranscriptRecord.Courses }
         *     
         */
        public Transcript.TranscriptRecord.Courses getCourses() {
            return courses;
        }

        /**
         * Sets the value of the courses property.
         * 
         * @param value
         *     allowed object is
         *     {@link Transcript.TranscriptRecord.Courses }
         *     
         */
        public void setCourses(Transcript.TranscriptRecord.Courses value) {
            this.courses = value;
        }

        /**
         * Gets the value of the personalNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPersonalNumber() {
            return personalNumber;
        }

        /**
         * Sets the value of the personalNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPersonalNumber(String value) {
            this.personalNumber = value;
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
         *         &lt;element name="course" maxOccurs="unbounded">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="code" type="{http://xml.netbeans.org/schema/commonSchema}courseCode"/>
         *                   &lt;element name="credits" type="{http://xml.netbeans.org/schema/commonSchema}credits"/>
         *                   &lt;element name="grade" type="{http://xml.netbeans.org/schema/commonSchema}grade"/>
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
        @XmlType(name = "", propOrder = { "course" })
        public static class Courses {

            @XmlElement(namespace = "http://xml.netbeans.org/schema/transcriptSchema", required = true)
            protected List<Transcript.TranscriptRecord.Courses.Course> course;

            /**
             * Gets the value of the course property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the course property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getCourse().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Transcript.TranscriptRecord.Courses.Course }
             * 
             * 
             */
            public List<Transcript.TranscriptRecord.Courses.Course> getCourse() {
                if (course == null) {
                    course = new ArrayList<Transcript.TranscriptRecord.Courses.Course>();
                }
                return this.course;
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
             *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="code" type="{http://xml.netbeans.org/schema/commonSchema}courseCode"/>
             *         &lt;element name="credits" type="{http://xml.netbeans.org/schema/commonSchema}credits"/>
             *         &lt;element name="grade" type="{http://xml.netbeans.org/schema/commonSchema}grade"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = { "name", "code", "credits", "grade" })
            public static class Course {

                @XmlElement(namespace = "http://xml.netbeans.org/schema/transcriptSchema", required = true)
                protected String name;

                @XmlElement(namespace = "http://xml.netbeans.org/schema/transcriptSchema", required = true)
                protected String code;

                @XmlElement(namespace = "http://xml.netbeans.org/schema/transcriptSchema", required = true)
                protected BigDecimal credits;

                @XmlElement(namespace = "http://xml.netbeans.org/schema/transcriptSchema", required = true)
                protected Grade grade;

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the code property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCode() {
                    return code;
                }

                /**
                 * Sets the value of the code property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCode(String value) {
                    this.code = value;
                }

                /**
                 * Gets the value of the credits property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigDecimal }
                 *     
                 */
                public BigDecimal getCredits() {
                    return credits;
                }

                /**
                 * Sets the value of the credits property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigDecimal }
                 *     
                 */
                public void setCredits(BigDecimal value) {
                    this.credits = value;
                }

                /**
                 * Gets the value of the grade property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Grade }
                 *     
                 */
                public Grade getGrade() {
                    return grade;
                }

                /**
                 * Sets the value of the grade property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Grade }
                 *     
                 */
                public void setGrade(Grade value) {
                    this.grade = value;
                }
            }
        }
    }
}
