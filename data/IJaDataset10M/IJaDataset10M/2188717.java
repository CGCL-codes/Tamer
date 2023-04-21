package org.yaoqiang.bpmn.model.elements.core.service;

import org.yaoqiang.bpmn.model.elements.core.foundation.BaseElement;
import org.yaoqiang.bpmn.model.elements.core.foundation.RootElement;
import org.yaoqiang.bpmn.model.elements.core.foundation.RootElements;

/**
 * EndPoint
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class EndPoint extends BaseElement implements RootElement {

    public EndPoint(RootElements parent) {
        super(parent, "endPoint");
    }
}
