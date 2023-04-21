package SevenZip.Compression.LZ;

import java.io.IOException;

public class InWindow {

    public byte[] _bufferBase;

    java.io.InputStream _stream;

    int _posLimit;

    boolean _streamEndWasReached;

    int _pointerToLastSafePosition;

    public int _bufferOffset;

    public int _blockSize;

    public int _pos;

    int _keepSizeBefore;

    int _keepSizeAfter;

    public int _streamPos;

    public void MoveBlock() {
        int offset = _bufferOffset + _pos - _keepSizeBefore;
        if (offset > 0) offset--;
        int numBytes = _bufferOffset + _streamPos - offset;
        for (int i = 0; i < numBytes; i++) _bufferBase[i] = _bufferBase[offset + i];
        _bufferOffset -= offset;
    }

    public void ReadBlock() throws IOException {
        if (_streamEndWasReached) return;
        while (true) {
            int size = (0 - _bufferOffset) + _blockSize - _streamPos;
            if (size == 0) return;
            int numReadBytes = _stream.read(_bufferBase, _bufferOffset + _streamPos, size);
            if (numReadBytes == -1) {
                _posLimit = _streamPos;
                int pointerToPostion = _bufferOffset + _posLimit;
                if (pointerToPostion > _pointerToLastSafePosition) _posLimit = _pointerToLastSafePosition - _bufferOffset;
                _streamEndWasReached = true;
                return;
            }
            _streamPos += numReadBytes;
            if (_streamPos >= _pos + _keepSizeAfter) _posLimit = _streamPos - _keepSizeAfter;
        }
    }

    void Free() {
        _bufferBase = null;
    }

    public void Create(int keepSizeBefore, int keepSizeAfter, int keepSizeReserv) {
        _keepSizeBefore = keepSizeBefore;
        _keepSizeAfter = keepSizeAfter;
        int blockSize = keepSizeBefore + keepSizeAfter + keepSizeReserv;
        if (_bufferBase == null || _blockSize != blockSize) {
            Free();
            _blockSize = blockSize;
            _bufferBase = new byte[_blockSize];
        }
        _pointerToLastSafePosition = _blockSize - keepSizeAfter;
    }

    public void SetStream(java.io.InputStream stream) {
        _stream = stream;
    }

    public void ReleaseStream() {
        _stream = null;
    }

    public void Init() throws IOException {
        _bufferOffset = 0;
        _pos = 0;
        _streamPos = 0;
        _streamEndWasReached = false;
        ReadBlock();
    }

    public void MovePos() throws IOException {
        _pos++;
        if (_pos > _posLimit) {
            int pointerToPostion = _bufferOffset + _pos;
            if (pointerToPostion > _pointerToLastSafePosition) MoveBlock();
            ReadBlock();
        }
    }

    public byte GetIndexByte(int index) {
        return _bufferBase[_bufferOffset + _pos + index];
    }

    public int GetMatchLen(int index, int distance, int limit) {
        if (_streamEndWasReached) if ((_pos + index) + limit > _streamPos) limit = _streamPos - (_pos + index);
        distance++;
        int pby = _bufferOffset + _pos + index;
        int i;
        for (i = 0; i < limit && _bufferBase[pby + i] == _bufferBase[pby + i - distance]; i++) ;
        return i;
    }

    public int GetNumAvailableBytes() {
        return _streamPos - _pos;
    }

    public void ReduceOffsets(int subValue) {
        _bufferOffset += subValue;
        _posLimit -= subValue;
        _pos -= subValue;
        _streamPos -= subValue;
    }
}
