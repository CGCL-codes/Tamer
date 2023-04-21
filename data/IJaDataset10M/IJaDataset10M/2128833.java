package net.jcc.protocol;

import java.io.IOException;
import net.jcc.sharedobj.SharedObjectPoolVisitor;
import net.jcc.util.ByteUtil;

/**
 *  
 *    Copyright [2009] [gaode]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author gaode
 * @email mr.gaode@gmail.com
 *
 */
public class ObjectGetRequestHandler implements Handler {

    SharedObjectPoolVisitor visitor;

    public ObjectGetRequestHandler(SharedObjectPoolVisitor visitor) {
        this.visitor = visitor;
    }

    public void dataReceived(DataPackage data) throws BadPackageException {
        byte[] b = data.getBytes();
        int id = ByteUtil.bytes2Int(b, 0);
        try {
            visitor.sendUpdate(id, data.getSource());
        } catch (IOException e) {
            throw new BadPackageException();
        }
    }

    public int getType() {
        return DataPackage.OBJECT_GET_REQUEST;
    }
}
