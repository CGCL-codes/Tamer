package com.acv.connector;

import java.io.Serializable;
import com.acv.common.exception.ConnectorException;
import com.acv.common.exception.UnsuccessfulRequestException;

/**
 * The general builder interface.
 * @author Bin Chen
 *
 */
public interface ConnectorBuilder {

    Serializable build() throws ConnectorException, UnsuccessfulRequestException;
}
