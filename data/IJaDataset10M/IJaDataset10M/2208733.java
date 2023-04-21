package com.mu.rai.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for targetControl complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="targetControl">
 *   &lt;complexContent>
 *     &lt;extension base="{http://services.rai.mu.com}modelObject">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "targetControl")
@XmlSeeAlso({ KernelTargetControl.class, ProcessTargetControl.class, ServiceTargetControl.class })
public abstract class TargetControl extends ModelObject {
}
