package org.openymsg.execute;

import org.openymsg.execute.dispatch.Dispatcher;
import org.openymsg.execute.read.PacketReader;
import org.openymsg.execute.write.PacketWriter;

public interface Executor extends PacketReader, PacketWriter, Dispatcher {
}
