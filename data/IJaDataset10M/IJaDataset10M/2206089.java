package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.pdu.PDUException;

/**
 * Преобразует байткод в соответствующую PDU.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 1, 2008
 * Time: 9:56:18 AM
 */
public interface PDUFactory {

    /**
     * Парсит полученный массив байтов и создает соответствующую PDU.
     *
     * @param bytes                         массив байтов
     * @return                              PDU
     * @throws PDUException                 ошибка обработки PDU
     */
    public PDU parsePDU(byte[] bytes) throws PDUException;
}
