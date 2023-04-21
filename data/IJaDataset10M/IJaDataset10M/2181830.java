package org.imsglobal.xsd.imsqti_v2p1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._1998.math.mathml.MathType;
import org.w3._2001.xinclude.Include;

/**
 * <p>Java class for DD.Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DD.Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}pre"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}h1"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}h2"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}h3"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}h4"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}h5"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}h6"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}p"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}address"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}dl"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}ol"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}ul"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}br"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}hr"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}img"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}printedVariable"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}object"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}rubricBlock"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}blockquote"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}feedbackBlock"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}hottext"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}em"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}a"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}code"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}span"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}sub"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}acronym"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}big"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}tt"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}kbd"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}q"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}i"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}dfn"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}feedbackInline"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}abbr"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}strong"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}sup"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}var"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}small"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}samp"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}b"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}cite"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}templateInline"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}templateBlock"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}table"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}div"/>
 *           &lt;element ref="{http://www.w3.org/1998/Math/MathML}math"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}textEntryInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}inlineChoiceInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}endAttemptInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}customInteraction"/>
 *           &lt;element ref="{http://www.w3.org/2001/XInclude}include"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}drawingInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}gapMatchInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}matchInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}graphicGapMatchInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}hotspotInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}graphicOrderInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}selectPointInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}graphicAssociateInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}sliderInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}choiceInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}mediaInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}hottextInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}orderInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}extendedTextInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}uploadInteraction"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}associateInteraction"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.imsglobal.org/xsd/imsqti_v2p1}Identifier.Type" />
 *       &lt;attribute name="class">
 *         &lt;simpleType>
 *           &lt;list itemType="{http://www.imsglobal.org/xsd/imsqti_v2p1}StringList.Type" />
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang"/>
 *       &lt;attribute name="label" type="{http://www.imsglobal.org/xsd/imsqti_v2p1}String256.Type" />
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}base"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DD.Type", propOrder = { "presAndH1SAndH2S" })
public class DDType {

    @XmlElementRefs({ @XmlElementRef(name = "orderInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = OrderInteractionType.class), @XmlElementRef(name = "h2", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "h5", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "dfn", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "templateBlock", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = TemplateBlockType.class), @XmlElementRef(name = "em", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "templateInline", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = TemplateInlineType.class), @XmlElementRef(name = "var", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "extendedTextInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = ExtendedTextInteractionType.class), @XmlElementRef(name = "uploadInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = UploadInteractionType.class), @XmlElementRef(name = "hottextInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = HottextInteractionType.class), @XmlElementRef(name = "ol", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "rubricBlock", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = RubricBlockType.class), @XmlElementRef(name = "choiceInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = ChoiceInteractionType.class), @XmlElementRef(name = "sub", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "graphicGapMatchInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = GraphicGapMatchInteractionType.class), @XmlElementRef(name = "graphicAssociateInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = GraphicAssociateInteractionType.class), @XmlElementRef(name = "kbd", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "samp", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "matchInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = MatchInteractionType.class), @XmlElementRef(name = "big", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "code", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "table", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = TableType.class), @XmlElementRef(name = "feedbackInline", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = FeedbackInlineType.class), @XmlElementRef(name = "associateInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = AssociateInteractionType.class), @XmlElementRef(name = "p", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "hr", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = HrType.class), @XmlElementRef(name = "i", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "tt", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "textEntryInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = TextEntryInteractionType.class), @XmlElementRef(name = "customInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = CustomInteractionType.class), @XmlElementRef(name = "img", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = ImgType.class), @XmlElementRef(name = "q", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = QType.class), @XmlElementRef(name = "acronym", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "address", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "cite", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "blockquote", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = BlockquoteType.class), @XmlElementRef(name = "object", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = ObjectType.class), @XmlElementRef(name = "inlineChoiceInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = InlineChoiceInteractionType.class), @XmlElementRef(name = "a", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = AType.class), @XmlElementRef(name = "drawingInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = DrawingInteractionType.class), @XmlElementRef(name = "hottext", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = HottextType.class), @XmlElementRef(name = "br", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = BrType.class), @XmlElementRef(name = "ul", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "gapMatchInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = GapMatchInteractionType.class), @XmlElementRef(name = "printedVariable", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = PrintedVariableType.class), @XmlElementRef(name = "pre", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "strong", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "graphicOrderInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = GraphicOrderInteractionType.class), @XmlElementRef(name = "abbr", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "sup", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "h6", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "math", namespace = "http://www.w3.org/1998/Math/MathML", type = MathType.class), @XmlElementRef(name = "h4", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "div", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = DivType.class), @XmlElementRef(name = "include", namespace = "http://www.w3.org/2001/XInclude", type = Include.class), @XmlElementRef(name = "b", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "feedbackBlock", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = FeedbackBlockType.class), @XmlElementRef(name = "dl", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = DlType.class), @XmlElementRef(name = "small", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "hotspotInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = HotspotInteractionType.class), @XmlElementRef(name = "selectPointInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = SelectPointInteractionType.class), @XmlElementRef(name = "sliderInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = SliderInteractionType.class), @XmlElementRef(name = "h3", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "span", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "endAttemptInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = EndAttemptInteractionType.class), @XmlElementRef(name = "h1", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "mediaInteraction", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = MediaInteractionType.class) })
    protected List<Object> presAndH1SAndH2S;

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String id;

    @XmlAttribute(name = "class")
    protected List<String> clazzs;

    @XmlAttribute(namespace = "http://www.w3.org/XML/1998/namespace")
    protected String lang;

    @XmlAttribute
    protected String label;

    @XmlAttribute(namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlSchemaType(name = "anyURI")
    protected String base;

    /**
     * Gets the value of the presAndH1SAndH2S property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the presAndH1SAndH2S property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPresAndH1SAndH2S().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrderInteractionType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link TemplateBlockType }
     * {@link TemplateInlineType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link ExtendedTextInteractionType }
     * {@link UploadInteractionType }
     * {@link HottextInteractionType }
     * {@link RubricBlockType }
     * {@link JAXBElement }{@code <}{@link OULType }{@code >}
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link ChoiceInteractionType }
     * {@link GraphicGapMatchInteractionType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link GraphicAssociateInteractionType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link MatchInteractionType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link TableType }
     * {@link FeedbackInlineType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link AssociateInteractionType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link HrType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link TextEntryInteractionType }
     * {@link ImgType }
     * {@link CustomInteractionType }
     * {@link QType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link ObjectType }
     * {@link BlockquoteType }
     * {@link InlineChoiceInteractionType }
     * {@link AType }
     * {@link HottextType }
     * {@link DrawingInteractionType }
     * {@link BrType }
     * {@link JAXBElement }{@code <}{@link OULType }{@code >}
     * {@link GapMatchInteractionType }
     * {@link PrintedVariableType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link GraphicOrderInteractionType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link MathType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link DivType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link Include }
     * {@link FeedbackBlockType }
     * {@link DlType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link HotspotInteractionType }
     * {@link SliderInteractionType }
     * {@link SelectPointInteractionType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link MediaInteractionType }
     * {@link JAXBElement }{@code <}{@link HTMLTextType }{@code >}
     * {@link EndAttemptInteractionType }
     * 
     * 
     */
    public List<Object> getPresAndH1SAndH2S() {
        if (presAndH1SAndH2S == null) {
            presAndH1SAndH2S = new ArrayList<Object>();
        }
        return this.presAndH1SAndH2S;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the clazzs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clazzs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClazzs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getClazzs() {
        if (clazzs == null) {
            clazzs = new ArrayList<String>();
        }
        return this.clazzs;
    }

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the base property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBase() {
        return base;
    }

    /**
     * Sets the value of the base property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBase(String value) {
        this.base = value;
    }
}
