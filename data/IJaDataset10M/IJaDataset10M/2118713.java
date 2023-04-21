package org.jefb.service;

import org.jefb.entity.dto.FileToken;
import org.jefb.entity.dto.TransmissionCallback;

public interface IReceiverService {

    TransmissionCallback receive(FileToken fileToken);
}
